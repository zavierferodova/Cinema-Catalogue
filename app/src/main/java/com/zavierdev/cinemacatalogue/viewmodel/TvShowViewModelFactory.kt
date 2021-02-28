package com.zavierdev.cinemacatalogue.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.di.RepositoryInjection
import com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow.DetailTvShowViewModel
import com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow.FavoriteTvShowViewModel
import com.zavierdev.cinemacatalogue.ui.home.tvshow.TvShowViewModel

class TvShowViewModelFactory private constructor(private val mTvShowRepository: TvShowRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: TvShowViewModelFactory? = null

        fun getInstance(context: Context): TvShowViewModelFactory = instance ?: synchronized(this) {
            instance ?: TvShowViewModelFactory(RepositoryInjection.provideTvShowRepository(context))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(TvShowViewModel::class.java) -> {
                return TvShowViewModel(mTvShowRepository) as T
            }
            modelClass.isAssignableFrom(DetailTvShowViewModel::class.java) -> {
                return DetailTvShowViewModel(mTvShowRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteTvShowViewModel::class.java) -> {
                return FavoriteTvShowViewModel(mTvShowRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}