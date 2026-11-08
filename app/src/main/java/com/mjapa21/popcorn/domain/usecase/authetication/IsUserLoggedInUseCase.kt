package com.mjapa21.popcorn.domain.usecase.authetication

import com.mjapa21.popcorn.domain.repository.AuthRepository

class IsUserLoggedInUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}