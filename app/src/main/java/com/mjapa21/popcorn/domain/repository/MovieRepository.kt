package com.mjapa21.popcorn.domain.repository

import com.mjapa21.popcorn.domain.model.MovieSearchResult

interface MovieRepository {
    suspend fun searchMovies(title: String, page: Int = 1): Result<MovieSearchResult>
}