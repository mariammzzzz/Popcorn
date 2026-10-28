package com.mjapa21.popcorn.domain.usecase

import com.mjapa21.popcorn.domain.repository.MovieRepository

class GetMovieDetailsUseCase(private val repo: MovieRepository) {
    suspend operator fun invoke(id: String) = repo.getMovieDetails(id)
}