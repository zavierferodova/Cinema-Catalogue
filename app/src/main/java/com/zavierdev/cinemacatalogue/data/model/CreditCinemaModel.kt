package com.zavierdev.cinemacatalogue.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreditCinemaModel(
    var playerImage: String,
    var playerName: String,
    var characterName: String
) : Parcelable