package com.mjapa21.popcorn.domain.model

data class MovieDetails(
    val id: String,
    val title: String,
    val year: String,
    val type: MediaType,
    val posterUrl: String?,
    val rated: String?,
    val released: String?,
    val runtime: String?,
    val genres: List<String>,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?,
    val language: String?,
    val country: String?,
    val awards: String?,
    val ratings: List<Rating>,
)

data class Rating(val source: String, val value: String)

fun MovieDetails.toMovie() =
    Movie(id = id, title = title, year = year, type = type, posterUrl = posterUrl)