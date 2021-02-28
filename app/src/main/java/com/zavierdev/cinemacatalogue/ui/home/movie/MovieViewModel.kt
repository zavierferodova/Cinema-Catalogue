package com.zavierdev.cinemacatalogue.ui.home.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.vo.Resource

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    fun getDiscoverMovie(): LiveData<Resource<ArrayList<DiscoverCinemaModel>>> =
        movieRepository.getDiscoverMovie()
}