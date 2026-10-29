package com.mjapa21.popcorn.domain.usecase.library

import com.mjapa21.popcorn.domain.repository.LibraryRepository

class ObserveLibraryUseCase(private val repo: LibraryRepository) {
    operator fun invoke() = repo.observeLibrary()
}