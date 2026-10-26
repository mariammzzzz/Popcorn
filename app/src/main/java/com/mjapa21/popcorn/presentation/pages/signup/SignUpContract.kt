package com.mjapa21.popcorn.presentation.pages.signup

data class SignUpState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
) {
    val canSubmit get() = email.isNotBlank() && password.isNotBlank()
}

sealed interface SignUpAction {
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data object OnSignUpClick : SignUpAction
    data object OnLoginClick : SignUpAction
}

sealed interface SignUpEvent {
    data object NavigateToHome : SignUpEvent
    data object NavigateToLogin : SignUpEvent
    data class ShowError(val message: String) : SignUpEvent
}