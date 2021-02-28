package com.zavierdev.cinemacatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.data.model.*
import com.zavierdev.cinemacatalogue.data.source.local.TvShowLocalData
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.data.source.remote.ApiResponse
import com.zavierdev.cinemacatalogue.data.source.remote.TvShowRemoteData
import com.zavierdev.cinemacatalogue.data.source.remote.api.APIConfig
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.TvShowCreditsResponse
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.TvShowDetailResponse
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.TvShowResultsItem
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.TvShowSimilarResponse
import com.zavierdev.cinemacatalogue.utils.AppExecutors
import com.zavierdev.cinemacatalogue.vo.Resource

class TvShowRepository(
    private val tvShowRemoteData: TvShowRemoteData,
    private val tvShowLocalData: TvShowLocalData,
    private val appExecutors: AppExecutors
) {
    companion object {
        @Volatile
        private var instance: TvShowRepository? = null

        fun getInstance(
            tvShowRemoteData: TvShowRemoteData,
            tvShowLocalData: TvShowLocalData,
            appExecutors: AppExecutors
        ): TvShowRepository =
            instance ?: synchronized(this) {
                instance ?: TvShowRepository(tvShowRemoteData, tvShowLocalData, appExecutors)
            }
    }

    /* ------------------ LOCAL DATA ------------------ */

    fun getFavoriteTvShows(): LiveData<PagedList<TvShowEntity>> {
        return LivePagedListBuilder(tvShowLocalData.getFavoriteTvShows(), 20).build()
    }

    fun getFavoriteTvShowById(id: Int): LiveData<TvShowEntity> {
        return tvShowLocalData.getFavoriteTvShowById(id)
    }

    fun insertFavoriteTvShow(tvShow: TvShowEntity) {
        appExecutors.diskIO().execute {
            tvShowLocalData.insertFavoriteTvShow(tvShow)
        }
    }

    fun deleteFavoriteTvShow(tvShow: TvShowEntity) {
        appExecutors.diskIO().execute {
            tvShowLocalData.deleteFavoriteTvShow(tvShow)
        }
    }

    /* ------------------ REMOTE DATA ------------------ */

    fun getDiscoverTvShow(): LiveData<Resource<ArrayList<DiscoverCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<DiscoverCinemaModel>, List<TvShowResultsItem>>(
                appExecutors
            ) {
            override fun createCall(): LiveData<ApiResponse<List<TvShowResultsItem>>> =
                tvShowRemoteData.getDiscoverTvShow()

            override fun provideResult(data: List<TvShowResultsItem>): LiveData<ArrayList<DiscoverCinemaModel>> {
                val mutableDiscoverCinema = MutableLiveData<ArrayList<DiscoverCinemaModel>>()
                val tvShows: ArrayList<DiscoverCinemaModel> = ArrayList()

                for (tvShow in data) {
                    val model = DiscoverCinemaModel(
                        tvShow.id,
                        tvShow.name,
                        tvShow.firstAirDate,
                        APIConfig.IMAGE_URL_MEDIUM + tvShow.posterPath
                    )
                    tvShows.add(model)
                }

                mutableDiscoverCinema.postValue(tvShows)
                return mutableDiscoverCinema
            }
        }.asLiveData()
    }

    fun getDetailTvShow(id: Int): LiveData<Resource<DetailCinemaModel>> {
        return object :
            NetworkBoundResource<DetailCinemaModel, TvShowDetailResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<TvShowDetailResponse>> =
                tvShowRemoteData.getDetailTvShow(id)

            override fun provideResult(data: TvShowDetailResponse): LiveData<DetailCinemaModel> {
                val mutableDetailCinema = MutableLiveData<DetailCinemaModel>()
                val genres: ArrayList<GenreCinemaModel> = ArrayList()

                for (genre in data.genres) {
                    val model = GenreCinemaModel(
                        genre.id,
                        genre.name
                    )
                    genres.add(model)
                }

                mutableDetailCinema.value = DetailCinemaModel(
                    data.id,
                    data.name,
                    genres,
                    data.firstAirDate,
                    if (data.episodeRunTime.isNotEmpty()) data.episodeRunTime[0]!! else 0,
                    data.status,
                    data.overview,
                    APIConfig.IMAGE_URL_LARGE + data.posterPath
                )

                return mutableDetailCinema
            }
        }.asLiveData()
    }

    fun getCreditsTvShow(id: Int): LiveData<Resource<ArrayList<CreditCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<CreditCinemaModel>, TvShowCreditsResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<TvShowCreditsResponse>> =
                tvShowRemoteData.getCreditsTvShow(id)

            override fun provideResult(data: TvShowCreditsResponse): LiveData<ArrayList<CreditCinemaModel>> {
                val mutableCreditsTvShow = MutableLiveData<ArrayList<CreditCinemaModel>>()
                val credits: ArrayList<CreditCinemaModel> = ArrayList()
                var counter = 0

                for (credit in data.cast) {
                    val model = CreditCinemaModel(
                        APIConfig.IMAGE_URL_SMALL + credit.profilePath,
                        credit.name,
                        credit.character
                    )
                    credits.add(model)
                    counter++

                    if (counter == 25)
                        break
                }

                mutableCreditsTvShow.value = credits
                return mutableCreditsTvShow
            }
        }.asLiveData()
    }

    fun getSimilarTvShows(id: Int): LiveData<Resource<ArrayList<SimilarCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<SimilarCinemaModel>, TvShowSimilarResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<TvShowSimilarResponse>> =
                tvShowRemoteData.getSimilarTvShows(id)

            override fun provideResult(data: TvShowSimilarResponse): LiveData<ArrayList<SimilarCinemaModel>> {
                val mutableSimilarTvShows = MutableLiveData<ArrayList<SimilarCinemaModel>>()
                val tvShows = ArrayList<SimilarCinemaModel>()

                for (tvShow in data.results) {
                    val model = SimilarCinemaModel(
                        tvShow.id,
                        tvShow.name,
                        tvShow.firstAirDate,
                        APIConfig.IMAGE_URL_SMALL + tvShow.posterPath
                    )
                    tvShows.add(model)
                }

                mutableSimilarTvShows.value = tvShows
                return mutableSimilarTvShows
            }
        }.asLiveData()
    }

    suspend fun getTvShowSearch(query: String): ArrayList<SearchCinemaModel> {
        val searchData = ArrayList<SearchCinemaModel>()
        val response = tvShowRemoteData.getTvShowSearch(query)

        if (response != null) {
            for (tvShow in response.results) {
                val model = SearchCinemaModel(
                    tvShow.id,
                    tvShow.name,
                    tvShow.firstAirDate,
                    tvShow.overview,
                    APIConfig.IMAGE_URL_MEDIUM + tvShow.posterPath,
                    CinemaType.TVSHOW
                )
                searchData.add(model)
            }
        }

        return searchData
    }
}