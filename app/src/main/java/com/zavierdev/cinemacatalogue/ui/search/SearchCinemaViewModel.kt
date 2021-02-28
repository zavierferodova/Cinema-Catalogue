package com.zavierdev.cinemacatalogue.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zavierdev.cinemacatalogue.data.model.SearchCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class SearchCinemaViewModel(
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) :
    ViewModel() {
    val searchData: MutableLiveData<ArrayList<SearchCinemaModel>> = MutableLiveData()

    suspend fun searchCinema(query: String) {
        val mergedData = ArrayList<SearchCinemaModel>()
        val movieData = GlobalScope.async {
            movieRepository.getMovieSearch(query)
        }
        val tvShowData = GlobalScope.async {
            tvShowRepository.getTvShowSearch(query)
        }

        mergedData.addAll(movieData.await())
        mergedData.addAll(tvShowData.await())

        if (mergedData.isEmpty())
            searchData.postValue(null)
        else
            searchData.postValue(mergedData)
    }
}