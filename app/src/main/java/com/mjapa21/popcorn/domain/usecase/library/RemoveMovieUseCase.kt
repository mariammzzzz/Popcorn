package com.mjapa21.popcorn.domain.usecase.library

import com.mjapa21.popcorn.domain.repository.LibraryRepository

class RemoveMovieUseCase(private val repo: LibraryRepository) {
    suspend operator fun invoke(id: String) = repo.remove(id)
}