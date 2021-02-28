package com.zavierdev.cinemacatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.data.source.local.room.CinemaDao

class TvShowLocalData private constructor(private val mCinemaDao: CinemaDao) {
    companion object {
        private var INSTANCE: TvShowLocalData? = null

        fun getInstance(cinemaDao: CinemaDao): TvShowLocalData =
            INSTANCE ?: TvShowLocalData(cinemaDao)
    }

    fun getFavoriteTvShows(): DataSource.Factory<Int, TvShowEntity> =
        mCinemaDao.getFavoriteTvShows()

    fun getFavoriteTvShowById(tvShowId: Int): LiveData<TvShowEntity> =
        mCinemaDao.getFavoriteTvShowById(tvShowId)

    fun insertFavoriteTvShow(tvShow: TvShowEntity) = mCinemaDao.insertFavoriteTvShow(tvShow)

    fun deleteFavoriteTvShow(tvShow: TvShowEntity) = mCinemaDao.deleteFavoriteTvShow(tvShow)
}