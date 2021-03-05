package com.zavierdev.cinemacatalogue.ui.detailcinema.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.test.CinemaDataGenerator
import com.zavierdev.cinemacatalogue.utils.RandomGenerator
import com.zavierdev.cinemacatalogue.vo.Resource
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailMovieViewModelTest {
    private lateinit var detailMovieViewModel: DetailMovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: MovieRepository

    @Before
    fun setup() {
        detailMovieViewModel = DetailMovieViewModel(movieRepository)
    }

    @Test
    fun getDetailMovie() {
        val observer: Observer<Resource<DetailCinemaModel>> =
            mock(Observer::class.java) as Observer<Resource<DetailCinemaModel>>
        val dummyDetailMovie = Resource.success(CinemaDataGenerator().getDetailCinema())
        val movie = MutableLiveData<Resource<DetailCinemaModel>>()
        movie.value = dummyDetailMovie

        `when`(movieRepository.getDetailMovie(dummyDetailMovie.data?.id!!)).thenReturn(movie)
        detailMovieViewModel.getRemoteDetailMovie(dummyDetailMovie.data?.id!!)
        verify(movieRepository).getDetailMovie(dummyDetailMovie.data?.id!!)

        // Observe Data
        detailMovieViewModel.getRemoteDetailMovie(dummyDetailMovie.data?.id!!)
            .observeForever(observer)
        verify(observer).onChanged(dummyDetailMovie)

        val detailMovie =
            detailMovieViewModel.getRemoteDetailMovie(dummyDetailMovie.data?.id!!).value?.data

        // Check not null
        assertNotNull(detailMovie)
        assertNotNull(detailMovie?.title)
        assertNotNull(detailMovie?.release)
        assertNotNull(detailMovie?.runtime)
        assertNotNull(detailMovie?.status)
        assertNotNull(detailMovie?.genres)
        assertNotNull(detailMovie?.overview)
        assertNotNull(detailMovie?.poster)

        // Check equality
        assertEquals(dummyDetailMovie.data?.title, detailMovie?.title)
        assertEquals(dummyDetailMovie.data?.release, detailMovie?.release)
        assertEquals(dummyDetailMovie.data?.runtime, detailMovie?.runtime)

        assertEquals(
            dummyDetailMovie.data?.genres?.size,
            detailMovie?.genres?.size
        )
        for (i in 0 until dummyDetailMovie.data?.genres?.size!!) {
            assertEquals(
                dummyDetailMovie.data?.genres?.get(i),
                detailMovie?.genres?.get(i)
            )
        }
        assertEquals(dummyDetailMovie.data?.status, detailMovie?.status)
        assertEquals(dummyDetailMovie.data?.overview, detailMovie?.overview)
        assertEquals(dummyDetailMovie.data?.poster, detailMovie?.poster)
    }

    @Test
    fun getCreditsMovie() {
        val observer: Observer<Resource<ArrayList<CreditCinemaModel>>> =
            mock(Observer::class.java) as Observer<Resource<ArrayList<CreditCinemaModel>>>
        val dummyCreditsMovie = Resource.success(CinemaDataGenerator().getCreditsCinema())
        val credits = MutableLiveData<Resource<ArrayList<CreditCinemaModel>>>()
        credits.value = dummyCreditsMovie

        val id = RandomGenerator().generateInteger(0, 1000)

        `when`(movieRepository.getCreditsMovie(id)).thenReturn(credits)
        detailMovieViewModel.getRemoteCreditsMovie(id)
        verify(movieRepository).getCreditsMovie(id)

        // Observe Data
        detailMovieViewModel.getRemoteCreditsMovie(id).observeForever(observer)
        verify(observer).onChanged(dummyCreditsMovie)

        val creditsMovie = detailMovieViewModel.getRemoteCreditsMovie(id).value?.data

        // Check not null
        assertNotNull(creditsMovie)
        for (i in 0 until dummyCreditsMovie.data?.size!!) {
            assertNotNull(creditsMovie?.get(i)?.playerImage)
            assertNotNull(creditsMovie?.get(i)?.playerName)
            assertNotNull(creditsMovie?.get(i)?.characterName)
        }

        // Check equality
        for (i in 0 until dummyCreditsMovie.data?.size!!) {
            assertEquals(dummyCreditsMovie.data!![i].playerImage, creditsMovie?.get(i)?.playerImage)
            assertEquals(dummyCreditsMovie.data!![i].playerName, creditsMovie?.get(i)?.playerName)
            assertEquals(
                dummyCreditsMovie.data!![i].characterName,
                creditsMovie?.get(i)?.characterName
            )
        }
    }

    @Test
    fun getFavoriteMovie() {
        val observer: Observer<MovieEntity> = mock(Observer::class.java) as Observer<MovieEntity>
        val dummyFavoriteMovie = CinemaDataGenerator().getSampleFavoriteMovie()
        val movie = MutableLiveData<MovieEntity>()
        movie.value = dummyFavoriteMovie

        `when`(movieRepository.getMovieFavoriteMovieById(dummyFavoriteMovie.id)).thenReturn(movie)
        val favoriteMovie = detailMovieViewModel.getFavoriteMovie(dummyFavoriteMovie.id).value
        verify(movieRepository).getMovieFavoriteMovieById(dummyFavoriteMovie.id)

        detailMovieViewModel.getFavoriteMovie(dummyFavoriteMovie.id).observeForever(observer)
        verify(observer).onChanged(dummyFavoriteMovie)

        assertNotNull(favoriteMovie)
        assertNotNull(favoriteMovie?.id)
        assertNotNull(favoriteMovie?.title)
        assertNotNull(favoriteMovie?.release)
        assertNotNull(favoriteMovie?.overview)
        assertNotNull(favoriteMovie?.poster)

        assertEquals(dummyFavoriteMovie.id, favoriteMovie?.id)
        assertEquals(dummyFavoriteMovie.title, favoriteMovie?.title)
        assertEquals(dummyFavoriteMovie.release, favoriteMovie?.release)
        assertEquals(dummyFavoriteMovie.overview, favoriteMovie?.overview)
        assertEquals(dummyFavoriteMovie.poster, favoriteMovie?.poster)
    }
}