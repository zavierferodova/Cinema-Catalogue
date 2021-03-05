package com.zavierdev.cinemacatalogue.ui.favorite.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.ui.home.favorite.tvshow.FavoriteTvShowViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavoriteTvShowViewModelTest {
    private lateinit var favoriteTvShowViewModel: FavoriteTvShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tvShowRepository: TvShowRepository

    @Mock
    private lateinit var pagedList: PagedList<TvShowEntity>

    @Mock
    private lateinit var observer: Observer<PagedList<TvShowEntity>>

    @Before
    fun setup() {
        favoriteTvShowViewModel = FavoriteTvShowViewModel(tvShowRepository)
    }

    @Test
    fun getFavoriteTvShows() {
        val dummyFavoriteTvShow = pagedList
        `when`(dummyFavoriteTvShow.size).thenReturn(10)
        val tvShows = MutableLiveData<PagedList<TvShowEntity>>()
        tvShows.value = dummyFavoriteTvShow

        `when`(tvShowRepository.getFavoriteTvShows()).thenReturn(tvShows)
        val favoriteTvShows = favoriteTvShowViewModel.getFavoriteTvShows().value
        verify(tvShowRepository).getFavoriteTvShows()

        favoriteTvShowViewModel.getFavoriteTvShows().observeForever(observer)
        verify(observer).onChanged(dummyFavoriteTvShow)

        assertNotNull(favoriteTvShows)
        assertEquals(10, favoriteTvShows?.size)
    }
}