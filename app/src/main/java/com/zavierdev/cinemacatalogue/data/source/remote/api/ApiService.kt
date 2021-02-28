package com.zavierdev.cinemacatalogue.data.source.remote.api

import com.zavierdev.cinemacatalogue.data.source.remote.response.movie.*
import com.zavierdev.cinemacatalogue.data.source.remote.response.tvshow.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/3/discover/movie")
    fun getDiscoverMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieDiscoverResponse>

    @GET("/3/movie/{id}")
    fun getDetailMovie(
        @Path("id") id: Int,
        @Query("api_key") api_key: String
    ): Call<MovieDetailResponse>

    @GET("/3/movie/{id}/credits")
    fun getCreditsMovie(
        @Path("id") id: Int,
        @Query("api_key") api_key: String
    ): Call<MovieCreditsResponse>

    @GET("3/movie/{id}/similar")
    fun getSimilarMovie(
        @Path("id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String = APIConfig.LANG,
        @Query("page") page: Int = 1
    ): Call<MovieSimilarResponse>

    @GET("/3/search/movie")
    fun getMovieSearch(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = APIConfig.LANG,
        @Query("include_adult") include_adult: Boolean = false
    ): Call<MovieSearchResponse>

    @GET("/3/discover/tv")
    fun getDiscoverTvShows(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<TvShowDiscoverResponse>

    @GET("/3/tv/{id}")
    fun getDetailTvShow(
        @Path("id") id: Int,
        @Query("api_key") api_key: String
    ): Call<TvShowDetailResponse>

    @GET("/3/tv/{id}/credits")
    fun getCreditsTvShow(
        @Path("id") id: Int,
        @Query("api_key") api_key: String
    ): Call<TvShowCreditsResponse>

    @GET("3/tv/{id}/similar")
    fun getSimilarTvShow(
        @Path("id") tv_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String = APIConfig.LANG,
        @Query("page") page: Int = 1
    ): Call<TvShowSimilarResponse>

    @GET("/3/search/tv")
    fun getTvShowSearch(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = APIConfig.LANG,
        @Query("include_adult") include_adult: Boolean = false
    ): Call<TvShowSearchResponse>
}