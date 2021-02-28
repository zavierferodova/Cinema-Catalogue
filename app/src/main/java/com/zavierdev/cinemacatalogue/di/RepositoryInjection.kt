package com.zavierdev.cinemacatalogue.di

import android.content.Context
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.source.local.MovieLocalData
import com.zavierdev.cinemacatalogue.data.source.local.TvShowLocalData
import com.zavierdev.cinemacatalogue.data.source.local.room.CinemaDatabase
import com.zavierdev.cinemacatalogue.data.source.remote.MovieRemoteData
import com.zavierdev.cinemacatalogue.data.source.remote.TvShowRemoteData
import com.zavierdev.cinemacatalogue.utils.AppExecutors

object RepositoryInjection {
    fun provideMovieRepository(context: Context): MovieRepository {
        val database = CinemaDatabase.getInstance(context)
        val movieRemoteData = MovieRemoteData.getInstance()
        val movieLocalData = MovieLocalData.getInstance(database.cinemaDao())
        val appExecutors = AppExecutors()

        return MovieRepository.getInstance(movieRemoteData, movieLocalData, appExecutors)
    }

    fun provideTvShowRepository(context: Context): TvShowRepository {
        val database = CinemaDatabase.getInstance(context)
        val tvShowRemoteData = TvShowRemoteData.getInstance()
        val tvShowLocalData = TvShowLocalData.getInstance(database.cinemaDao())
        val appExecutors = AppExecutors()

        return TvShowRepository.getInstance(tvShowRemoteData, tvShowLocalData, appExecutors)
    }
}