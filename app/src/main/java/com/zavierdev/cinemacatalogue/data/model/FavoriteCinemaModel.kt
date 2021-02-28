package com.zavierdev.cinemacatalogue.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteCinemaModel(
    val id: Int,
    val title: String,
    val release: String? = null,
    val overview: String,
    val poster: String
) : Parcelable