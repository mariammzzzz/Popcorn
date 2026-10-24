package com.mjapa21.popcorn.presentation.pages.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(value: String) {
        state = state.copy(email = value, emailError = null)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value, passwordError = null)
    }

    //todo implement properly
    fun validate(): Boolean {
        val emailValid = Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches()
        val passwordValid = state.password.length >= 6
        state = state.copy(
            emailError = if (emailValid) null else "Enter a valid email address",
            passwordError = if (passwordValid) null else "Password must be at least 6 characters",
        )
        return emailValid && passwordValid
    }
}