package com.zavierdev.cinemacatalogue.ui.detailcinema.tvshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.model.FavoriteCinemaModel
import com.zavierdev.cinemacatalogue.data.model.SimilarCinemaModel
import com.zavierdev.cinemacatalogue.data.source.TvShowRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.utils.DateConveter
import com.zavierdev.cinemacatalogue.vo.Resource

class DetailTvShowViewModel(private val tvShowRepository: TvShowRepository) : ViewModel() {
    fun getFavoriteTvShow(id: Int): LiveData<TvShowEntity> =
        tvShowRepository.getFavoriteTvShowById(id)

    fun getRemoteDetailTvShow(id: Int): LiveData<Resource<DetailCinemaModel>> =
        tvShowRepository.getDetailTvShow(id)

    fun getRemoteCreditsTvShow(id: Int): LiveData<Resource<ArrayList<CreditCinemaModel>>> =
        tvShowRepository.getCreditsTvShow(id)

    fun getRemoteSimilarTvShows(id: Int): LiveData<Resource<ArrayList<SimilarCinemaModel>>> =
        tvShowRepository.getSimilarTvShows(id)

    fun insertFavoriteCinema(favoriteCinema: FavoriteCinemaModel) {
        val currentTimestamp = System.currentTimeMillis()
        val favoriteTvShow = TvShowEntity(
            favoriteCinema.id,
            favoriteCinema.title,
            favoriteCinema.release,
            favoriteCinema.overview,
            favoriteCinema.poster,
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
        tvShowRepository.insertFavoriteTvShow(favoriteTvShow)
    }

    fun deleteFavoriteCinema(favoriteCinema: FavoriteCinemaModel) {
        val currentTimestamp = System.currentTimeMillis()
        val favoriteTvShow = TvShowEntity(
            favoriteCinema.id,
            favoriteCinema.title,
            favoriteCinema.release,
            favoriteCinema.overview,
            favoriteCinema.poster,
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
        tvShowRepository.deleteFavoriteTvShow(favoriteTvShow)
    }
}