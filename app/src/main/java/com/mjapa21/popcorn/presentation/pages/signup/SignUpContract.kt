package com.mjapa21.popcorn.presentation.pages.signup

data class SignUpState(
    val fullName: String = "",
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val fullNameError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
) {
    val canSubmit get() =
        fullName.isNotBlank() && username.isNotBlank() && password.isNotBlank()
}

sealed interface SignUpAction {
    data class OnFullNameChange(val fullName: String) : SignUpAction
    data class OnUsernameChange(val username: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data object OnSignUpClick : SignUpAction
    data object OnLoginClick : SignUpAction
}

sealed interface SignUpEvent {
    data object NavigateToHome : SignUpEvent
    data object NavigateToLogin : SignUpEvent
    data class ShowError(val message: String) : SignUpEvent
}