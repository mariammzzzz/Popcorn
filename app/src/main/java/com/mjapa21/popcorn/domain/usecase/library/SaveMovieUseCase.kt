package com.mjapa21.popcorn.domain.usecase.library

import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.WatchStatus
import com.mjapa21.popcorn.domain.repository.LibraryRepository

class SaveMovieUseCase(private val repo: LibraryRepository) {
    suspend operator fun invoke(movie: Movie, status: WatchStatus) = repo.save(movie, status)
}