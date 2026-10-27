package com.mjapa21.popcorn.domain.model

enum class WatchStatus { WATCHLIST, WATCHED }

data class SavedMovie(
    val id: String,
    val title: String,
    val year: String,
    val posterUrl: String?,
    val type: MediaType,
    val status: WatchStatus,
)