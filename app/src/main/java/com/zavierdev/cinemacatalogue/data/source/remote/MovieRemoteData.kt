package com.zavierdev.cinemacatalogue.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zavierdev.cinemacatalogue.data.source.remote.api.APIConfig
import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRemoteData() {
    companion object {
        @Volatile
        private var instance: MovieRemoteData? = null

        fun getInstance(): MovieRemoteData = instance ?: synchronized(this) {
            instance ?: MovieRemoteData()
        }
    }

    fun getDiscoverMovie(): LiveData<ApiResponse<List<MovieResultsItem>>> {
        val resultDiscoverMovies = MutableLiveData<ApiResponse<List<MovieResultsItem>>>();
        val client = APIConfig.getApiService().getDiscoverMovies(APIConfig.API_KEY, APIConfig.LANG)

        client.enqueue(object : Callback<MovieDiscoverResponse> {
            override fun onResponse(
                call: Call<MovieDiscoverResponse>,
                response: Response<MovieDiscoverResponse>
            ) {
                if (response.isSuccessful) {
                    resultDiscoverMovies.value = ApiResponse.success(response.body()?.results!!)
                } else {
                    resultDiscoverMovies.value =
                        ApiResponse.error("Failed to get discovered movie data", listOf())
                }
            }

            override fun onFailure(call: Call<MovieDiscoverResponse>, t: Throwable) {
                resultDiscoverMovies.value =
                    ApiResponse.error("Failed to get discovered movie data", listOf())
            }
        })

        return resultDiscoverMovies
    }

    fun getDetailMovie(id: Int): LiveData<ApiResponse<MovieDetailResponse>> {
        val resultDetailMovie = MutableLiveData<ApiResponse<MovieDetailResponse>>()
        val client = APIConfig.getApiService().getDetailMovie(id, APIConfig.API_KEY)

        client.enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    resultDetailMovie.value = ApiResponse.success(response.body()!!)
                } else {
                    resultDetailMovie.value =
                        ApiResponse.error("Failed to get detail movie data", response.body()!!)
                }
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                resultDetailMovie.value =
                    ApiResponse.error("Failed to get detail movie data", MovieDetailResponse())
            }
        })

        return resultDetailMovie
    }

    fun getCreditsMovie(id: Int): LiveData<ApiResponse<MovieCreditsResponse>> {
        val resultCreditsMovie = MutableLiveData<ApiResponse<MovieCreditsResponse>>()
        val client = APIConfig.getApiService().getCreditsMovie(id, APIConfig.API_KEY)

        client.enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(
                call: Call<MovieCreditsResponse>,
                response: Response<MovieCreditsResponse>
            ) {
                if (response.isSuccessful) {
                    resultCreditsMovie.value = ApiResponse.success(response.body()!!)
                } else {
                    resultCreditsMovie.value =
                        ApiResponse.error("Failed to get credits movie data", response.body()!!)
                }
            }

            override fun onFailure(call: Call<MovieCreditsResponse>, t: Throwable) {
                resultCreditsMovie.value =
                    ApiResponse.error("Failed to get credits movie data", MovieCreditsResponse())
            }
        })

        return resultCreditsMovie
    }

    fun getSimilarMovies(id: Int): LiveData<ApiResponse<MovieSimilarResponse>> {
        val resultSimilarMovies = MutableLiveData<ApiResponse<MovieSimilarResponse>>()
        val client =
            APIConfig.getApiService().getSimilarMovie(id, APIConfig.API_KEY, APIConfig.LANG)

        client.enqueue(object : Callback<MovieSimilarResponse> {
            override fun onResponse(
                call: Call<MovieSimilarResponse>,
                response: Response<MovieSimilarResponse>
            ) {
                if (response.isSuccessful) {
                    resultSimilarMovies.value = ApiResponse.success(response.body()!!)
                } else {
                    resultSimilarMovies.value = ApiResponse.success(response.body()!!)
                }
            }

            override fun onFailure(call: Call<MovieSimilarResponse>, t: Throwable) {
                resultSimilarMovies.value =
                    ApiResponse.error("Failed to get similar movie data", MovieSimilarResponse())
            }
        })

        return resultSimilarMovies
    }

    suspend fun getMovieSearch(query: String): MovieSearchResponse? {
        val client = APIConfig.getApiService().getMovieSearch(APIConfig.API_KEY, query)

        val result = GlobalScope.async {
            try {
                client.execute().body()
            } catch (e: Exception) {
                MovieSearchResponse()
            }
        }

        return result.await()
    }
}