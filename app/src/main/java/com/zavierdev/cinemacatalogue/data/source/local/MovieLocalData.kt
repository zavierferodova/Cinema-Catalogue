package com.zavierdev.cinemacatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.local.room.CinemaDao

class MovieLocalData private constructor(private val mCinemaDao: CinemaDao) {
    companion object {
        private var INSTANCE: MovieLocalData? = null

        fun getInstance(cinemaDao: CinemaDao): MovieLocalData =
            INSTANCE ?: MovieLocalData(cinemaDao)
    }

    fun getFavoriteMovies(): DataSource.Factory<Int, MovieEntity> = mCinemaDao.getFavoriteMovies()

    fun getFavoriteMovieById(movieId: Int): LiveData<MovieEntity> =
        mCinemaDao.getFavoriteMovieById(movieId)

    fun insertFavoriteMovie(movie: MovieEntity) = mCinemaDao.insertFavoriteMovie(movie)

    fun deleteFavoriteMovie(movie: MovieEntity) = mCinemaDao.deleteFavoriteMovie(movie)
}