package com.mjapa21.popcorn.domain.usecase.authetication

import com.mjapa21.popcorn.domain.repository.AuthRepository

class SignOutUseCase(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.signOut()
}