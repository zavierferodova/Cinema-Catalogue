package com.zavierdev.cinemacatalogue.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailCinemaModel(
    var id: Int,
    var title: String,
    var genres: ArrayList<GenreCinemaModel>,
    var release: String?,
    var runtime: Int,
    var status: String,
    var overview: String,
    var poster: String,
) : Parcelable