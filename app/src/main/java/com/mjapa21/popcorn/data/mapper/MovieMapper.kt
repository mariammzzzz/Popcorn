package com.mjapa21.popcorn.data.mapper

import com.mjapa21.popcorn.data.model.MovieDto
import com.mjapa21.popcorn.data.model.SearchResponseDto
import com.mjapa21.popcorn.domain.model.MediaType
import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.MovieSearchResult

fun MovieDto.toDomain() = Movie(
    id = imdbId,
    title = title,
    year = year,
    type = when (type.lowercase()) {
        "movie" -> MediaType.MOVIE
        "series" -> MediaType.SERIES
        "episode" -> MediaType.EPISODE
        "game" -> MediaType.GAME
        else -> MediaType.UNKNOWN
    },
    posterUrl = poster.takeIf { it != "N/A" },
)

fun SearchResponseDto.toDomain() = MovieSearchResult(
    movies = search.orEmpty().map { it.toDomain() },
    totalResults = totalResults?.toIntOrNull() ?: 0,
)