package com.zavierdev.cinemacatalogue.ui.favorite.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.verify
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.ui.home.favorite.movie.FavoriteMovieViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteMovieViewModelTest {
    private lateinit var favoriteMovieViewModel: FavoriteMovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Mock
    private lateinit var pagedList: PagedList<MovieEntity>

    @Mock
    private lateinit var observer: Observer<PagedList<MovieEntity>>

    @Before
    fun setup() {
        favoriteMovieViewModel = FavoriteMovieViewModel(movieRepository)
    }

    @Test
    fun getFavoriteMovies() {
        val dummyFavoriteMovie = pagedList
        `when`(dummyFavoriteMovie.size).thenReturn(10)
        val movies = MutableLiveData<PagedList<MovieEntity>>()
        movies.value = dummyFavoriteMovie

        `when`(movieRepository.getFavoriteMovies()).thenReturn(movies)
        val favoriteMovies = favoriteMovieViewModel.getFavoriteMovies().value
        verify(movieRepository).getFavoriteMovies()

        favoriteMovieViewModel.getFavoriteMovies().observeForever(observer)
        verify(observer).onChanged(dummyFavoriteMovie)

        assertNotNull(favoriteMovies)
        assertEquals(10, favoriteMovies?.size)
    }
}