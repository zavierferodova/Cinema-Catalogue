package com.zavierdev.cinemacatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity

@Dao
interface CinemaDao {
    /* -------------------- MOVIE -------------------- */

    @Query("SELECT * FROM favorite_movie ORDER BY saved_at DESC")
    fun getFavoriteMovies(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM favorite_movie WHERE id = :movieId")
    fun getFavoriteMovieById(movieId: Int): LiveData<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteMovie(movie: MovieEntity)

    @Update
    fun updateFavoriteMovie(movie: MovieEntity)

    @Delete
    fun deleteFavoriteMovie(movie: MovieEntity)

    /* -------------------- TV SHOW -------------------- */

    @Query("SELECT * FROM favorite_tv_show ORDER BY saved_at DESC")
    fun getFavoriteTvShows(): DataSource.Factory<Int, TvShowEntity>

    @Query("SELECT * FROM favorite_tv_show WHERE id = :tvShowId")
    fun getFavoriteTvShowById(tvShowId: Int): LiveData<TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteTvShow(tvShow: TvShowEntity)

    @Update
    fun updateFavoriteTvShow(tvShow: TvShowEntity)

    @Delete
    fun deleteFavoriteTvShow(tvShow: TvShowEntity)
}