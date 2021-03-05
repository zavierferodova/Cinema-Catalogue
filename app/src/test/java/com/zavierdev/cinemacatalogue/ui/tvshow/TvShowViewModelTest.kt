package com.zavierdev.cinemacatalogue.ui.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.test.CinemaDataGenerator
import com.zavierdev.cinemacatalogue.ui.home.tvshow.TvShowViewModel
import com.zavierdev.cinemacatalogue.vo.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowViewModelTest {
    private lateinit var tvShowViewModel: TvShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tvShowRepository: TvShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<ArrayList<DiscoverCinemaModel>>>

    @Before
    fun setup() {
        tvShowViewModel = TvShowViewModel(tvShowRepository)
    }

    @Test
    fun getDiscoverTvShows() {
        val dummyTvShows = Resource.success(CinemaDataGenerator().getDiscoverSample())
        val tvShows = MutableLiveData<Resource<ArrayList<DiscoverCinemaModel>>>()
        tvShows.value = dummyTvShows

        Mockito.`when`(tvShowRepository.getDiscoverTvShow()).thenReturn(tvShows)
        val tvShowDiscover = tvShowViewModel.getDiscoverTvShow().value?.data
        Mockito.verify(tvShowRepository).getDiscoverTvShow()

        assertNotNull(tvShowDiscover)
        assertEquals(10, tvShowDiscover?.size)

        tvShowViewModel.getDiscoverTvShow().observeForever(observer)
        Mockito.verify(observer).onChanged(dummyTvShows)
    }
}