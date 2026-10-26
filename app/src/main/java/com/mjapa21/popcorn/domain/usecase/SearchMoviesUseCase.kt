package com.mjapa21.popcorn.domain.usecase

import com.mjapa21.popcorn.domain.model.MovieSearchResult
import com.mjapa21.popcorn.domain.repository.MovieRepository

class SearchMoviesUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(title: String, page: Int = 1): Result<MovieSearchResult> {
        return movieRepository.searchMovies(title, page)
    }
}