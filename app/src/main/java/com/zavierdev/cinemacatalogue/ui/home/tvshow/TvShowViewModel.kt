package com.zavierdev.cinemacatalogue.ui.home.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.vo.Resource

class TvShowViewModel(private val tvShowRepository: TvShowRepository) : ViewModel() {
    fun getDiscoverTvShow(): LiveData<Resource<ArrayList<DiscoverCinemaModel>>> =
        tvShowRepository.getDiscoverTvShow()
}