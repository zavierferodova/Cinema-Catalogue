package com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity

class FavoriteTvShowViewModel(private val tvShowRepository: TvShowRepository) : ViewModel() {
    fun getFavoriteTvShows(): LiveData<PagedList<TvShowEntity>> {
        return tvShowRepository.getFavoriteTvShows()
    }
}