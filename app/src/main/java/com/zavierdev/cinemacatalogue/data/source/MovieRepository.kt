package com.zavierdev.cinemacatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zavierdev.cinemacatalogue.constant.CinemaType
import com.zavierdev.cinemacatalogue.data.model.*
import com.zavierdev.cinemacatalogue.data.source.local.MovieLocalData
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.remote.ApiResponse
import com.zavierdev.cinemacatalogue.data.source.remote.MovieRemoteData
import com.zavierdev.cinemacatalogue.data.source.remote.api.APIConfig
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.MovieCreditsResponse
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.MovieDetailResponse
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.MovieResultsItem
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.MovieSimilarResponse
import com.zavierdev.cinemacatalogue.utils.AppExecutors
import com.zavierdev.cinemacatalogue.vo.Resource


class MovieRepository(
    private val movieRemoteData: MovieRemoteData,
    private val movieLocalData: MovieLocalData,
    private val appExecutors: AppExecutors
) {
    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(
            movieRemoteData: MovieRemoteData,
            movieLocalData: MovieLocalData,
            appExecutors: AppExecutors
        ): MovieRepository =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(movieRemoteData, movieLocalData, appExecutors)
            }
    }

    /* ------------------ LOCAL DATA ------------------ */

    fun getFavoriteMovies(): LiveData<PagedList<MovieEntity>> {
        val config = PagedList.Config.Builder().apply {
            setEnablePlaceholders(false)
            setInitialLoadSizeHint(4)
            setPageSize(4)
        }.build()
        return LivePagedListBuilder(movieLocalData.getFavoriteMovies(), config).build()
    }

    fun getMovieFavoriteMovieById(id: Int): LiveData<MovieEntity> {
        return movieLocalData.getFavoriteMovieById(id)
    }

    fun insertFavoriteMovie(movie: MovieEntity) {
        appExecutors.diskIO().execute {
            movieLocalData.insertFavoriteMovie(movie)
        }
    }

    fun deleteFavoriteMovie(movie: MovieEntity) {
        appExecutors.diskIO().execute {
            movieLocalData.deleteFavoriteMovie(movie)
        }
    }

    /* ------------------ REMOTE DATA ------------------ */

    fun getDiscoverMovie(): LiveData<Resource<ArrayList<DiscoverCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<DiscoverCinemaModel>, List<MovieResultsItem>>(
                appExecutors
            ) {
            override fun createCall(): LiveData<ApiResponse<List<MovieResultsItem>>> =
                movieRemoteData.getDiscoverMovie()

            override fun provideResult(data: List<MovieResultsItem>): LiveData<ArrayList<DiscoverCinemaModel>> {
                val mutableDiscoverCinema = MutableLiveData<ArrayList<DiscoverCinemaModel>>()
                val movies: ArrayList<DiscoverCinemaModel> = ArrayList()

                for (movie in data) {
                    val model = DiscoverCinemaModel(
                        movie.id,
                        movie.title,
                        movie.releaseDate,
                        APIConfig.IMAGE_URL_MEDIUM + movie.posterPath
                    )
                    movies.add(model)
                }

                mutableDiscoverCinema.postValue(movies)

                return mutableDiscoverCinema
            }
        }.asLiveData()
    }

    fun getDetailMovie(id: Int): LiveData<Resource<DetailCinemaModel>> {
        return object : NetworkBoundResource<DetailCinemaModel, MovieDetailResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<MovieDetailResponse>> =
                movieRemoteData.getDetailMovie(id)

            override fun provideResult(data: MovieDetailResponse): LiveData<DetailCinemaModel> {
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
                    data.title,
                    genres,
                    data.releaseDate,
                    data.runtime,
                    data.status,
                    data.overview,
                    APIConfig.IMAGE_URL_LARGE + data.posterPath
                )

                return mutableDetailCinema
            }
        }.asLiveData()
    }

    fun getCreditsMovie(id: Int): LiveData<Resource<ArrayList<CreditCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<CreditCinemaModel>, MovieCreditsResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<MovieCreditsResponse>> =
                movieRemoteData.getCreditsMovie(id)

            override fun provideResult(data: MovieCreditsResponse): LiveData<ArrayList<CreditCinemaModel>> {
                val mutableCreditsMovie = MutableLiveData<ArrayList<CreditCinemaModel>>()
                val credits: ArrayList<CreditCinemaModel> = ArrayList()
                var counter = 0;

                for (credit in data.cast) {
                    val model = CreditCinemaModel(
                        APIConfig.IMAGE_URL_SMALL + credit.profilePath,
                        credit.name,
                        credit.character ?: "Unknown"
                    )
                    credits.add(model)
                    counter++

                    if (counter == 30)
                        break
                }

                mutableCreditsMovie.value = credits
                return mutableCreditsMovie
            }
        }.asLiveData()
    }

    fun getSimilarMovies(id: Int): LiveData<Resource<ArrayList<SimilarCinemaModel>>> {
        return object :
            NetworkBoundResource<ArrayList<SimilarCinemaModel>, MovieSimilarResponse>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<MovieSimilarResponse>> =
                movieRemoteData.getSimilarMovies(id)

            override fun provideResult(data: MovieSimilarResponse): LiveData<ArrayList<SimilarCinemaModel>> {
                val mutableSimilarMovies = MutableLiveData<ArrayList<SimilarCinemaModel>>()
                val movies = ArrayList<SimilarCinemaModel>()

                for (movie in data.results) {
                    val model = SimilarCinemaModel(
                        movie.id,
                        movie.title,
                        movie.releaseDate,
                        APIConfig.IMAGE_URL_SMALL + movie.posterPath
                    )
                    movies.add(model)
                }

                mutableSimilarMovies.value = movies
                return mutableSimilarMovies
            }
        }.asLiveData()
    }

    suspend fun getMovieSearch(query: String): ArrayList<SearchCinemaModel> {
        val searchData = ArrayList<SearchCinemaModel>()
        val response = movieRemoteData.getMovieSearch(query)

        if (response != null) {
            for (movie in response.results) {
                val model = SearchCinemaModel(
                    movie.id,
                    movie.title,
                    movie.releaseDate,
                    movie.overview,
                    APIConfig.IMAGE_URL_MEDIUM + movie.posterPath,
                    CinemaType.MOVIE
                )
                searchData.add(model)
            }
        }

        return searchData
    }
}