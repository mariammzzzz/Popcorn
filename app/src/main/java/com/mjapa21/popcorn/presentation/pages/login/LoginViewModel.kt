package com.mjapa21.popcorn.presentation.pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.validation.UserDataValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val eventChannel = Channel<LoginEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnUsernameChange ->
                _state.update { it.copy(username = action.username, usernameError = null) }

            is LoginAction.OnPasswordChange ->
                _state.update { it.copy(password = action.password, passwordError = null) }

            LoginAction.OnLoginClick -> login()

            LoginAction.OnSignUpClick ->
                viewModelScope.launch { eventChannel.send(LoginEvent.NavigateToSignUp) }
        }
    }

    private fun login() {
        val current = _state.value
        if (current.isLoading || !current.canSubmit || !validate()) return

        val username = UserDataValidator.normalizeUsername(current.username)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // TODO: real auth call with `username` + current.password
                delay(1_500)
                eventChannel.send(LoginEvent.NavigateToHome)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                eventChannel.send(LoginEvent.ShowError("Invalid username or password"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(): Boolean {
        val s = _state.value
        val usernameError = UserDataValidator.usernameError(s.username)
        val passwordError = UserDataValidator.passwordError(s.password)
        _state.update { it.copy(usernameError = usernameError, passwordError = passwordError) }
        return usernameError == null && passwordError == null
    }
}