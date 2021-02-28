package com.zavierdev.cinemacatalogue.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.test.CinemaDataGenerator
import com.zavierdev.cinemacatalogue.ui.home.movie.MovieViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {
    private lateinit var movieViewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var observer: Observer<ArrayList<DiscoverCinemaModel>>

    @Before
    fun setup() {
        movieViewModel = MovieViewModel(movieRepository)
    }

    @Test
    fun getDiscoverMovies() {
        val dummyMovies = CinemaDataGenerator().getDiscoverSample()
        val movies = MutableLiveData<ArrayList<DiscoverCinemaModel>>()
        movies.value = dummyMovies

        `when`(movieRepository.getDiscoverMovie()).thenReturn(movies)
        val movieDiscover = movieViewModel.getDiscoverMovie().value
        verify(movieRepository).getDiscoverMovie()

        assertNotNull(movieDiscover)
        assertEquals(10, movieDiscover?.size)

        movieViewModel.getDiscoverMovie().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}