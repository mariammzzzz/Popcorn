package com.mjapa21.popcorn.domain.usecase.library

import com.mjapa21.popcorn.domain.model.WatchStatus
import com.mjapa21.popcorn.domain.repository.LibraryRepository

class UpdateMovieStatusUseCase(private val repo: LibraryRepository) {
    suspend operator fun invoke(id: String, status: WatchStatus) = repo.updateStatus(id, status)
}