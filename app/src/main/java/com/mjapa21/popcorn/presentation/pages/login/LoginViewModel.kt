package com.mjapa21.popcorn.presentation.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.usecase.LoginUseCase
import com.mjapa21.popcorn.domain.validation.UserDataValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val eventChannel = Channel<LoginEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange ->
                _state.update {
                    it.copy(
                        email = UserDataValidator.sanitizeEmail(action.email),
                        emailError = null
                    )
                }

            is LoginAction.OnPasswordChange ->
                _state.update {
                    it.copy(
                        password = UserDataValidator.sanitizePassword(action.password),
                        passwordError = null
                    )
                }

            LoginAction.OnLoginClick -> login()

            LoginAction.OnSignUpClick ->
                viewModelScope.launch { eventChannel.send(LoginEvent.NavigateToSignUp) }
        }
    }

    private fun login() {
        val current = _state.value
        if (current.isLoading || !current.canSubmit || !validate()) return

        val email = UserDataValidator.normalizeEmail(current.email)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            loginUseCase(email, current.password).apply {
                onSuccess {
                    eventChannel.send(LoginEvent.NavigateToHome)
                }
                onFailure {
                    eventChannel.send(
                        LoginEvent.ShowError(
                            it.message ?: "Unknown error"
                        )
                    ) //todo maybe use hardcoded error message not to expose internal error messages to the user
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun validate(): Boolean {
        val s = _state.value
        val emailError = UserDataValidator.emailError(s.email)
        val passwordError = UserDataValidator.passwordError(s.password)
        _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
        return emailError == null && passwordError == null
    }
}