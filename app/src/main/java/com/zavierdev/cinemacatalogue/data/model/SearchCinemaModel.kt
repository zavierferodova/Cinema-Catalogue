package com.zavierdev.cinemacatalogue.data.model

import com.zavierdev.cinemacatalogue.constant.CinemaType

data class SearchCinemaModel(
    val id: Int,
    val title: String,
    val release: String? = "",
    val overview: String,
    val poster: String? = "",
    val type: CinemaType
)
