package com.mjapa21.popcorn.presentation.pages

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
) {
    val canSubmit get() = email.isNotBlank() && password.isNotBlank()
}