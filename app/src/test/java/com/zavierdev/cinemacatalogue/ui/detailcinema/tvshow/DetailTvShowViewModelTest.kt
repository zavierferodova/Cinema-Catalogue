package com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
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
class DetailTvShowViewModelTest {
    private lateinit var detailTvShowViewModel: DetailTvShowViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var tvShowRepository: TvShowRepository

    @Before
    fun setup() {
        detailTvShowViewModel = DetailTvShowViewModel(tvShowRepository)
    }

    @Test
    fun getDetailTvShow() {
        val observer: Observer<Resource<DetailCinemaModel>> =
            mock(Observer::class.java) as Observer<Resource<DetailCinemaModel>>
        val dummyDetailTvShow = Resource.success(CinemaDataGenerator().getDetailCinema())
        val tvShow = MutableLiveData<Resource<DetailCinemaModel>>()
        tvShow.value = dummyDetailTvShow

        `when`(tvShowRepository.getDetailTvShow(dummyDetailTvShow.data?.id!!)).thenReturn(tvShow)
        detailTvShowViewModel.getRemoteDetailTvShow(dummyDetailTvShow.data?.id!!)
        verify(tvShowRepository).getDetailTvShow(dummyDetailTvShow.data?.id!!)

        // Observe Data
        detailTvShowViewModel.getRemoteDetailTvShow(dummyDetailTvShow.data?.id!!)
            .observeForever(observer)
        verify(observer).onChanged(dummyDetailTvShow)

        val detailTvShow =
            detailTvShowViewModel.getRemoteDetailTvShow(dummyDetailTvShow.data?.id!!).value?.data

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
        assertEquals(dummyDetailTvShow.data?.title, detailTvShow?.title)
        assertEquals(dummyDetailTvShow.data?.release, detailTvShow?.release)
        assertEquals(dummyDetailTvShow.data?.runtime, detailTvShow?.runtime)
        assertEquals(
            dummyDetailTvShow.data?.genres?.size,
            detailTvShow?.genres?.size
        )
        for (i in 0 until dummyDetailTvShow.data?.genres?.size!!) {
            assertEquals(
                dummyDetailTvShow.data?.genres?.get(i),
                detailTvShow?.genres?.get(i)
            )
        }
        assertEquals(dummyDetailTvShow.data?.status, detailTvShow?.status)
        assertEquals(dummyDetailTvShow.data?.overview, detailTvShow?.overview)
        assertEquals(dummyDetailTvShow.data?.poster, detailTvShow?.poster)
    }

    @Test
    fun getCreditsTvShow() {
        val observer: Observer<Resource<ArrayList<CreditCinemaModel>>> =
            mock(Observer::class.java) as Observer<Resource<ArrayList<CreditCinemaModel>>>
        val dummyCreditsTvShow = Resource.success(CinemaDataGenerator().getCreditsCinema())
        val credits = MutableLiveData<Resource<ArrayList<CreditCinemaModel>>>()
        credits.value = dummyCreditsTvShow

        val id = RandomGenerator().generateInteger(0, 1000)

        `when`(tvShowRepository.getCreditsTvShow(id)).thenReturn(credits)
        detailTvShowViewModel.getRemoteCreditsTvShow(id)
        verify(tvShowRepository).getCreditsTvShow(id)

        // Observe Data
        detailTvShowViewModel.getRemoteCreditsTvShow(id).observeForever(observer)
        verify(observer).onChanged(dummyCreditsTvShow)

        val creditsTvShow = detailTvShowViewModel.getRemoteCreditsTvShow(id).value?.data

        // Check not null
        assertNotNull(creditsTvShow)
        for (i in 0 until dummyCreditsTvShow.data?.size!!) {
            assertNotNull(creditsTvShow?.get(i)?.playerImage)
            assertNotNull(creditsTvShow?.get(i)?.playerName)
            assertNotNull(creditsTvShow?.get(i)?.characterName)
        }

        // Check equality
        for (i in 0 until dummyCreditsTvShow.data?.size!!) {
            assertEquals(
                dummyCreditsTvShow.data!![i].playerImage,
                creditsTvShow?.get(i)?.playerImage
            )
            assertEquals(dummyCreditsTvShow.data!![i].playerName, creditsTvShow?.get(i)?.playerName)
            assertEquals(
                dummyCreditsTvShow.data!![i].characterName,
                creditsTvShow?.get(i)?.characterName
            )
        }
    }

    @Test
    fun getFavoriteTvShow() {
        val observer: Observer<TvShowEntity> = mock(Observer::class.java) as Observer<TvShowEntity>
        val dummyFavoriteTvShow = CinemaDataGenerator().getSampleFavoriteTvShow()
        val tvShow = MutableLiveData<TvShowEntity>()
        tvShow.value = dummyFavoriteTvShow

        `when`(tvShowRepository.getFavoriteTvShowById(dummyFavoriteTvShow.id)).thenReturn(tvShow)
        val favoriteTvShow = detailTvShowViewModel.getFavoriteTvShow(dummyFavoriteTvShow.id).value
        verify(tvShowRepository).getFavoriteTvShowById(dummyFavoriteTvShow.id)

        detailTvShowViewModel.getFavoriteTvShow(dummyFavoriteTvShow.id).observeForever(observer)
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