package com.mjapa21.popcorn.data.model

import androidx.annotation.Keep

@Keep
data class SavedMovieDto(
    val title: String = "",
    val year: String = "",
    val posterUrl: String? = null,
    val type: String = "UNKNOWN",
    val status: String = "WATCHLIST",
    val addedAt: Long = 0L,
)