package com.mjapa21.popcorn.data.mapper

import com.mjapa21.popcorn.data.model.MovieDetailsDto
import com.mjapa21.popcorn.data.model.MovieDto
import com.mjapa21.popcorn.data.model.SavedMovieDto
import com.mjapa21.popcorn.data.model.SearchResponseDto
import com.mjapa21.popcorn.domain.model.MediaType
import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.MovieDetails
import com.mjapa21.popcorn.domain.model.MovieSearchResult
import com.mjapa21.popcorn.domain.model.Rating
import com.mjapa21.popcorn.domain.model.SavedMovie
import com.mjapa21.popcorn.domain.model.WatchStatus

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


private fun String?.orNull(): String? = this?.takeIf { it.isNotBlank() && it != "N/A" }

fun MovieDetailsDto.toDomain() = MovieDetails(
    id = imdbId.orEmpty(),
    title = title.orEmpty(),
    year = year.orEmpty(),
    type = when (type?.lowercase()) {
        "movie" -> MediaType.MOVIE
        "series" -> MediaType.SERIES
        "episode" -> MediaType.EPISODE
        "game" -> MediaType.GAME
        else -> MediaType.UNKNOWN
    },
    posterUrl = poster.orNull(),
    rated = rated.orNull(),
    released = released.orNull(),
    runtime = runtime.orNull(),
    genres = genre.orNull()?.split(",")?.map { it.trim() }.orEmpty(),
    director = director.orNull(),
    writer = writer.orNull(),
    actors = actors.orNull(),
    plot = plot.orNull(),
    language = language.orNull(),
    country = country.orNull(),
    awards = awards.orNull(),
    ratings = ratings.orEmpty().map { Rating(it.source, it.value) },
)

fun SavedMovieDto.toDomain(id: String) = SavedMovie(
    id = id,
    title = title,
    year = year,
    posterUrl = posterUrl,
    type = runCatching { MediaType.valueOf(type) }.getOrDefault(MediaType.UNKNOWN),
    status = runCatching { WatchStatus.valueOf(status) }.getOrDefault(WatchStatus.WATCHLIST),
    addedAt = addedAt,
)