package com.zavierdev.cinemacatalogue.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreCinemaModel(
    var id: Int,
    var name: String
): Parcelable