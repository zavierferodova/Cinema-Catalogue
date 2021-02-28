package com.zavierdev.cinemacatalogue.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.di.RepositoryInjection
import com.zavierdev.cinemacatalogue.ui.search.SearchCinemaViewModel

class CinemaViewModelFactory private constructor(
    private val mMovieRepository: MovieRepository,
    private val mTvShowRepository: TvShowRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: CinemaViewModelFactory? = null

        fun getInstance(context: Context): CinemaViewModelFactory = instance ?: synchronized(this) {
            instance ?: CinemaViewModelFactory(
                RepositoryInjection.provideMovieRepository(context),
                RepositoryInjection.provideTvShowRepository(context)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SearchCinemaViewModel::class.java) -> {
                return SearchCinemaViewModel(mMovieRepository, mTvShowRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}