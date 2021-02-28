package com.zavierdev.cinemacatalogue.data.source.remote.api

import com.zavierdev.cinemacatalogue.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    companion object {
        val API_KEY = BuildConfig.TMDB_API_KEY
        val LANG = "en-US"
        val BASE_URL: String = "https://api.themoviedb.org"
        val IMAGE_URL_SMALL: String = "https://image.tmdb.org/t/p/w185"
        val IMAGE_URL_MEDIUM: String = "https://image.tmdb.org/t/p/w342"
        val IMAGE_URL_LARGE: String = "https://image.tmdb.org/t/p/w500"

        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}