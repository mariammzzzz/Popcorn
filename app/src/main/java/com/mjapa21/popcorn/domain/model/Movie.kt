package com.mjapa21.popcorn.domain.model

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val type: MediaType,
    val posterUrl: String?,
)

enum class MediaType {
    MOVIE, SERIES, EPISODE, GAME, UNKNOWN
}

data class MovieSearchResult(
    val movies: List<Movie>,
    val totalResults: Int,
)