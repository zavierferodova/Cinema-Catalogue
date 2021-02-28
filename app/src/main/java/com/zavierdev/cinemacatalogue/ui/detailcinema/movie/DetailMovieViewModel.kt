package com.zavierdev.cinemacatalogue.ui.detailcinema.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.model.FavoriteCinemaModel
import com.zavierdev.cinemacatalogue.data.model.SimilarCinemaModel
import com.zavierdev.cinemacatalogue.data.source.MovieRepository
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.utils.DateConveter
import com.zavierdev.cinemacatalogue.vo.Resource

class DetailMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    fun getFavoriteMovie(id: Int): LiveData<MovieEntity> =
        movieRepository.getMovieFavoriteMovieById(id)

    fun getRemoteDetailMovie(id: Int): LiveData<Resource<DetailCinemaModel>> =
        movieRepository.getDetailMovie(id)

    fun getRemoteCreditsMovie(id: Int): LiveData<Resource<ArrayList<CreditCinemaModel>>> =
        movieRepository.getCreditsMovie(id)

    fun getSimilarMovies(id: Int): LiveData<Resource<ArrayList<SimilarCinemaModel>>> =
        movieRepository.getSimilarMovies(id)

    fun insertFavoriteCinema(favoriteCinema: FavoriteCinemaModel) {
        val currentTimestamp = System.currentTimeMillis()
        val favoriteMovie = MovieEntity(
            favoriteCinema.id,
            favoriteCinema.title,
            favoriteCinema.release,
            favoriteCinema.overview,
            favoriteCinema.poster,
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
        movieRepository.insertFavoriteMovie(favoriteMovie)
    }

    fun deleteFavoriteCinema(favoriteCinema: FavoriteCinemaModel) {
        val currentTimestamp = System.currentTimeMillis()
        val favoriteMovie = MovieEntity(
            favoriteCinema.id,
            favoriteCinema.title,
            favoriteCinema.release,
            favoriteCinema.overview,
            favoriteCinema.poster,
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
        movieRepository.deleteFavoriteMovie(favoriteMovie)
    }
}