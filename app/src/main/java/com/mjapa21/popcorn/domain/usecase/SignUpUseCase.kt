package com.mjapa21.popcorn.domain.usecase

import com.mjapa21.popcorn.domain.repository.AuthRepository

class SignUpUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return authRepository.signUp(email, password)
    }
}