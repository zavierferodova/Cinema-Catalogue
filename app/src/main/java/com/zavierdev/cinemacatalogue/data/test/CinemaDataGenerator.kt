package com.zavierdev.cinemacatalogue.data.test

import com.zavierdev.cinemacatalogue.data.model.CreditCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DetailCinemaModel
import com.zavierdev.cinemacatalogue.data.model.DiscoverCinemaModel
import com.zavierdev.cinemacatalogue.data.model.GenreCinemaModel
import com.zavierdev.cinemacatalogue.data.source.local.entity.MovieEntity
import com.zavierdev.cinemacatalogue.data.source.local.entity.TvShowEntity
import com.zavierdev.cinemacatalogue.utils.DateConveter
import com.zavierdev.cinemacatalogue.utils.RandomGenerator

class CinemaDataGenerator {
    fun getDiscoverSample(): ArrayList<DiscoverCinemaModel> {
        val randomData: ArrayList<DiscoverCinemaModel> = ArrayList()

        for (i in 0 until 10) {
            randomData.add(
                DiscoverCinemaModel(
                    RandomGenerator().generateInteger(0, 1000),
                    RandomGenerator().generateString(50),
                    RandomGenerator().generateString(10),
                    RandomGenerator().generateString(128),
                )
            )
        }

        return randomData
    }

    fun getDetailCinema(): DetailCinemaModel {
        val categorySample: ArrayList<GenreCinemaModel> = ArrayList()
        for (i in 0 until 5) {
            categorySample.add(
                GenreCinemaModel(
                    RandomGenerator().generateInteger(0, 100),
                    RandomGenerator().generateString(128)
                )
            )
        }

        return DetailCinemaModel(
            RandomGenerator().generateInteger(0, 1000),
            RandomGenerator().generateString(128),
            categorySample,
            RandomGenerator().generateString(128),
            RandomGenerator().generateInteger(0, 120),
            RandomGenerator().generateString(20),
            RandomGenerator().generateString(300),
            RandomGenerator().generateString(128)
        )
    }

    fun getCreditsCinema(): ArrayList<CreditCinemaModel> {
        val sampleCredits = ArrayList<CreditCinemaModel>()

        for (i in 0..5) {
            sampleCredits.add(
                CreditCinemaModel(
                    RandomGenerator().generateString(128),
                    RandomGenerator().generateString(64),
                    RandomGenerator().generateString(64)
                )
            )
        }

        return sampleCredits
    }

    fun getSampleFavoriteMovie(): MovieEntity {
        val currentTimestamp = System.currentTimeMillis()
        return MovieEntity(
            RandomGenerator().generateInteger(0, 10),
            RandomGenerator().generateString(64),
            RandomGenerator().generateString(10),
            RandomGenerator().generateString(300),
            RandomGenerator().generateString(128),
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
    }

    fun getFavoriteMovies(): ArrayList<MovieEntity> {
        val favoriteMovies = ArrayList<MovieEntity>()

        for (i in 0 until 10) {
            val currentTimestamp = System.currentTimeMillis()
            favoriteMovies.add(
                MovieEntity(
                    RandomGenerator().generateInteger(0, 10),
                    RandomGenerator().generateString(64),
                    RandomGenerator().generateString(10),
                    RandomGenerator().generateString(300),
                    RandomGenerator().generateString(128),
                    DateConveter().fromTimestamp(currentTimestamp)!!
                )
            )
        }

        return favoriteMovies
    }

    fun getFavoriteTvShows(): ArrayList<TvShowEntity> {
        val favoriteTvShow = ArrayList<TvShowEntity>()

        for (i in 0 until 10) {
            val currentTimestamp = System.currentTimeMillis()
            favoriteTvShow.add(
                TvShowEntity(
                    RandomGenerator().generateInteger(0, 10),
                    RandomGenerator().generateString(64),
                    RandomGenerator().generateString(10),
                    RandomGenerator().generateString(300),
                    RandomGenerator().generateString(128),
                    DateConveter().fromTimestamp(currentTimestamp)!!
                )
            )
        }

        return favoriteTvShow
    }

    fun getSampleFavoriteTvShow(): TvShowEntity {
        val currentTimestamp = System.currentTimeMillis()
        return TvShowEntity(
            RandomGenerator().generateInteger(0, 10),
            RandomGenerator().generateString(64),
            RandomGenerator().generateString(10),
            RandomGenerator().generateString(300),
            RandomGenerator().generateString(128),
            DateConveter().fromTimestamp(currentTimestamp)!!
        )
    }
}