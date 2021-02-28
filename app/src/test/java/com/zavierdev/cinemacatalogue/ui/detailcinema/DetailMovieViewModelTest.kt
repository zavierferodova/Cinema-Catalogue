package com.zavierdev.cinemacatalogue.ui.detailcinema

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.data.test.CinemaDataGenerator
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity.Companion.CINEMA_MOVIE
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieActivity.Companion.CINEMA_TV
import com.zavierdev.cinemacatalogue.ui.detailcinema.movie.DetailMovieViewModel
import com.zavierdev.cinemacatalogue.utils.RandomGenerator
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
        val observer: Observer<DetailCinemaModel> =
            mock(Observer::class.java) as Observer<DetailCinemaModel>
        val dummyDetailMovie = CinemaDataGenerator().getDetailCinema()
        val movie = MutableLiveData<DetailCinemaModel>()
        movie.value = dummyDetailMovie

        `when`(movieRepository.getDetailMovie(dummyDetailMovie.id)).thenReturn(movie)
        detailMovieViewModel.updateCinemaData(dummyDetailMovie.id, CINEMA_MOVIE)
        verify(movieRepository).getDetailMovie(dummyDetailMovie.id)

        // Observe Data
        detailMovieViewModel.cinema.observeForever(observer)
        verify(observer).onChanged(dummyDetailMovie)

        val detailMovie = detailMovieViewModel.cinema.value

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
        assertEquals(dummyDetailMovie.title, detailMovie?.title)
        assertEquals(dummyDetailMovie.release, detailMovie?.release)
        assertEquals(dummyDetailMovie.runtime, detailMovie?.runtime)
        assertEquals(
            dummyDetailMovie.genres.size,
            detailMovie?.genres?.size
        )
        for (i in 0 until dummyDetailMovie.genres.size) {
            assertEquals(
                dummyDetailMovie.genres.get(i),
                detailMovie?.genres?.get(i)
            )
        }
        assertEquals(dummyDetailMovie.status, detailMovie?.status)
        assertEquals(dummyDetailMovie.overview, detailMovie?.overview)
        assertEquals(dummyDetailMovie.poster, detailMovie?.poster)
    }

    @Test
    fun getCreditsMovie() {
        val observer: Observer<ArrayList<CreditCinemaModel>> =
            mock(Observer::class.java) as Observer<ArrayList<CreditCinemaModel>>
        val dummyCreditsMovie = CinemaDataGenerator().getCreditsCinema()
        val credits = MutableLiveData<ArrayList<CreditCinemaModel>>()
        credits.value = dummyCreditsMovie

        val id = RandomGenerator().generateInteger(0, 1000)

        `when`(movieRepository.getCreditsMovie(id)).thenReturn(credits)
        detailMovieViewModel.updateCinemaData(id, CINEMA_MOVIE)
        verify(movieRepository).getCreditsMovie(id)

        // Observe Data
        detailMovieViewModel.credits.observeForever(observer)
        verify(observer).onChanged(dummyCreditsMovie)

        val creditsMovie = detailMovieViewModel.credits.value

        // Check not null
        assertNotNull(creditsMovie)
        for (i in 0 until dummyCreditsMovie.size) {
            assertNotNull(creditsMovie?.get(i)?.playerImage)
            assertNotNull(creditsMovie?.get(i)?.playerName)
            assertNotNull(creditsMovie?.get(i)?.characterName)
        }

        // Check equality
        for (i in 0 until dummyCreditsMovie.size) {
            assertEquals(dummyCreditsMovie[i].playerImage, creditsMovie?.get(i)?.playerImage)
            assertEquals(dummyCreditsMovie[i].playerName, creditsMovie?.get(i)?.playerName)
            assertEquals(dummyCreditsMovie[i].characterName, creditsMovie?.get(i)?.characterName)
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

    @Test
    fun getDetailTvShow() {
        val observer: Observer<DetailCinemaModel> =
            mock(Observer::class.java) as Observer<DetailCinemaModel>
        val dummyDetailTvShow = CinemaDataGenerator().getDetailCinema()
        val tvShow = MutableLiveData<DetailCinemaModel>()
        tvShow.value = dummyDetailTvShow

        `when`(movieRepository.getDetailTvShow(dummyDetailTvShow.id)).thenReturn(tvShow)
        detailMovieViewModel.updateCinemaData(dummyDetailTvShow.id, CINEMA_TV)
        verify(movieRepository).getDetailTvShow(dummyDetailTvShow.id)

        // Observe Data
        detailMovieViewModel.cinema.observeForever(observer)
        verify(observer).onChanged(dummyDetailTvShow)

        val detailTvShow = detailMovieViewModel.cinema.value

        // Check not null
        assertNotNull(detailTvShow)
        assertNotNull(detailTvShow?.title)
        assertNotNull(detailTvShow?.release)
        assertNotNull(detailTvShow?.runtime)
        assertNotNull(detailTvShow?.status)
        assertNotNull(detailTvShow?.genres)
        assertNotNull(detailTvShow?.overview)
        assertNotNull(detailTvShow?.poster)

        // Check equality
        assertEquals(dummyDetailTvShow.title, detailTvShow?.title)
        assertEquals(dummyDetailTvShow.release, detailTvShow?.release)
        assertEquals(dummyDetailTvShow.runtime, detailTvShow?.runtime)
        assertEquals(
            dummyDetailTvShow.genres.size,
            detailTvShow?.genres?.size
        )
        for (i in 0 until dummyDetailTvShow.genres.size) {
            assertEquals(
                dummyDetailTvShow.genres.get(i),
                detailTvShow?.genres?.get(i)
            )
        }
        assertEquals(dummyDetailTvShow.status, detailTvShow?.status)
        assertEquals(dummyDetailTvShow.overview, detailTvShow?.overview)
        assertEquals(dummyDetailTvShow.poster, detailTvShow?.poster)
    }

    @Test
    fun getCreditsTvShow() {
        val observer: Observer<ArrayList<CreditCinemaModel>> =
            mock(Observer::class.java) as Observer<ArrayList<CreditCinemaModel>>
        val dummyCreditsTvShow = CinemaDataGenerator().getCreditsCinema()
        val credits = MutableLiveData<ArrayList<CreditCinemaModel>>()
        credits.value = dummyCreditsTvShow

        val id = RandomGenerator().generateInteger(0, 1000)

        `when`(movieRepository.getCreditsTvShow(id)).thenReturn(credits)
        detailMovieViewModel.updateCinemaData(id, CINEMA_TV)
        verify(movieRepository).getCreditsTvShow(id)

        // Observe Data
        detailMovieViewModel.credits.observeForever(observer)
        verify(observer).onChanged(dummyCreditsTvShow)

        val creditsTvShow = detailMovieViewModel.credits.value

        // Check not null
        assertNotNull(creditsTvShow)
        for (i in 0 until dummyCreditsTvShow.size) {
            assertNotNull(creditsTvShow?.get(i)?.playerImage)
            assertNotNull(creditsTvShow?.get(i)?.playerName)
            assertNotNull(creditsTvShow?.get(i)?.characterName)
        }

        // Check equality
        for (i in 0 until dummyCreditsTvShow.size) {
            assertEquals(dummyCreditsTvShow[i].playerImage, creditsTvShow?.get(i)?.playerImage)
            assertEquals(dummyCreditsTvShow[i].playerName, creditsTvShow?.get(i)?.playerName)
            assertEquals(dummyCreditsTvShow[i].characterName, creditsTvShow?.get(i)?.characterName)
        }
    }

    @Test
    fun getFavoriteTvShow() {
        val observer: Observer<TvShowEntity> = mock(Observer::class.java) as Observer<TvShowEntity>
        val dummyFavoriteTvShow = CinemaDataGenerator().getSampleFavoriteTvShow()
        val tvShow = MutableLiveData<TvShowEntity>()
        tvShow.value = dummyFavoriteTvShow

        `when`(movieRepository.getFavoriteTvShowById(dummyFavoriteTvShow.id)).thenReturn(tvShow)
        val favoriteTvShow = detailMovieViewModel.getFavoriteTvShow(dummyFavoriteTvShow.id).value
        verify(movieRepository).getFavoriteTvShowById(dummyFavoriteTvShow.id)

        detailMovieViewModel.getFavoriteTvShow(dummyFavoriteTvShow.id).observeForever(observer)
        verify(observer).onChanged(dummyFavoriteTvShow)

        assertNotNull(favoriteTvShow)
        assertNotNull(favoriteTvShow?.id)
        assertNotNull(favoriteTvShow?.title)
        assertNotNull(favoriteTvShow?.release)
        assertNotNull(favoriteTvShow?.overview)
        assertNotNull(favoriteTvShow?.poster)

        assertEquals(dummyFavoriteTvShow.id, favoriteTvShow?.id)
        assertEquals(dummyFavoriteTvShow.title, favoriteTvShow?.title)
        assertEquals(dummyFavoriteTvShow.release, favoriteTvShow?.release)
        assertEquals(dummyFavoriteTvShow.overview, favoriteTvShow?.overview)
        assertEquals(dummyFavoriteTvShow.poster, favoriteTvShow?.poster)
    }
}