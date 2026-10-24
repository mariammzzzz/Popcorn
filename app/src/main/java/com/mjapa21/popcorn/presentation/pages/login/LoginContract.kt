package com.mjapa21.popcorn.presentation.pages.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()
}


sealed interface LoginAction {
    data class OnUsernameChange(val username: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnLoginClick : LoginAction
    data object OnSignUpClick : LoginAction
}


sealed interface LoginEvent {
    data object NavigateToHome : LoginEvent
    data object NavigateToSignUp : LoginEvent
    data class ShowError(val message: String) : LoginEvent
}
