package com.zavierdev.cinemacatalogue.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zavierdev.cinemacatalogue.data.source.remote.api.APIConfig
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.MovieSearchResponse
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TvShowRemoteData() {
    companion object {
        @Volatile
        private var instance: TvShowRemoteData? = null

        fun getInstance(): TvShowRemoteData = instance ?: synchronized(this) {
            instance ?: TvShowRemoteData()
        }
    }

    fun getDiscoverTvShow(): LiveData<ApiResponse<List<TvShowResultsItem>>> {
        val resultDiscoverTvShows = MutableLiveData<ApiResponse<List<TvShowResultsItem>>>();
        val client = APIConfig.getApiService().getDiscoverTvShows(APIConfig.API_KEY, APIConfig.LANG)

        client.enqueue(object : Callback<TvShowDiscoverResponse> {
            override fun onResponse(
                call: Call<TvShowDiscoverResponse>,
                response: Response<TvShowDiscoverResponse>
            ) {
                if (response.isSuccessful) {
                    resultDiscoverTvShows.value = ApiResponse.success(response.body()?.results!!)
                } else {
                    resultDiscoverTvShows.value =
                        ApiResponse.error("Failed to get discovered tv show data", listOf())
                }
            }

            override fun onFailure(call: Call<TvShowDiscoverResponse>, t: Throwable) {
                resultDiscoverTvShows.value =
                    ApiResponse.error("Failed to get discovered tv show data", listOf())
            }
        })

        return resultDiscoverTvShows
    }

    fun getDetailTvShow(id: Int): LiveData<ApiResponse<TvShowDetailResponse>> {
        val resultDetailTvShow = MutableLiveData<ApiResponse<TvShowDetailResponse>>()
        val client = APIConfig.getApiService().getDetailTvShow(id, APIConfig.API_KEY)

        client.enqueue(object : Callback<TvShowDetailResponse> {
            override fun onResponse(
                call: Call<TvShowDetailResponse>,
                response: Response<TvShowDetailResponse>
            ) {
                if (response.isSuccessful) {
                    resultDetailTvShow.value = ApiResponse.success(response.body()!!)
                } else {
                    resultDetailTvShow.value =
                        ApiResponse.error("Failed to get detail tv show data", TvShowDetailResponse())
                }
            }

            override fun onFailure(call: Call<TvShowDetailResponse>, t: Throwable) {
                resultDetailTvShow.value =
                    ApiResponse.error("Failed to get detail tv show data", TvShowDetailResponse())
            }
        })

        return resultDetailTvShow
    }

    fun getCreditsTvShow(id: Int): LiveData<ApiResponse<TvShowCreditsResponse>> {
        val resultCreditsTvShow = MutableLiveData<ApiResponse<TvShowCreditsResponse>>()
        val client = APIConfig.getApiService().getCreditsTvShow(id, APIConfig.API_KEY)

        client.enqueue(object : Callback<TvShowCreditsResponse> {
            override fun onResponse(
                call: Call<TvShowCreditsResponse>,
                response: Response<TvShowCreditsResponse>
            ) {
                if (response.isSuccessful) {
                    resultCreditsTvShow.value = ApiResponse.success(response.body()!!)
                } else {
                    resultCreditsTvShow.value =
                        ApiResponse.error(
                            "Failed to get credits tv show data",
                            TvShowCreditsResponse()
                        )
                }
            }

            override fun onFailure(call: Call<TvShowCreditsResponse>, t: Throwable) {
                resultCreditsTvShow.value =
                    ApiResponse.error("Failed to get credits tv show data", TvShowCreditsResponse())
            }
        })

        return resultCreditsTvShow
    }

    fun getSimilarTvShows(id: Int): LiveData<ApiResponse<TvShowSimilarResponse>> {
        val resultSimilarTvShows = MutableLiveData<ApiResponse<TvShowSimilarResponse>>()
        val client =
            APIConfig.getApiService().getSimilarTvShow(id, APIConfig.API_KEY, APIConfig.LANG)

        client.enqueue(object : Callback<TvShowSimilarResponse> {
            override fun onResponse(
                call: Call<TvShowSimilarResponse>,
                response: Response<TvShowSimilarResponse>
            ) {
                if (response.isSuccessful) {
                    resultSimilarTvShows.value = ApiResponse.success(response.body()!!)
                } else {
                    resultSimilarTvShows.value =
                        ApiResponse.error("Failed to get similar tv show data", TvShowSimilarResponse())
                }
            }

            override fun onFailure(call: Call<TvShowSimilarResponse>, t: Throwable) {
                resultSimilarTvShows.value =
                    ApiResponse.error("Failed to get similar tv show data", TvShowSimilarResponse())
            }
        })

        return resultSimilarTvShows
    }

    suspend fun getTvShowSearch(query: String): TvShowSearchResponse? {
        val client = APIConfig.getApiService().getTvShowSearch(APIConfig.API_KEY, query)

        val result = GlobalScope.async {
            try {
                client.execute().body()
            } catch(e: Exception) {
                TvShowSearchResponse()
            }
        }

        return result.await()
    }
}
