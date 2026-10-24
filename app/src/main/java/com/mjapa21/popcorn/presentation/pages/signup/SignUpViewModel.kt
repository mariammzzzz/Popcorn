package com.mjapa21.popcorn.presentation.pages.signup

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

class SignUpViewModel : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    //Buffered so send() doesn’t suspend while the UI isn’t collecting (e.g. during rotation)
    private val eventChannel = Channel<SignUpEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {

            is SignUpAction.OnFullNameChange ->
                _state.update { it.copy(fullName = action.fullName, fullNameError = null) }

            is SignUpAction.OnUsernameChange ->
                _state.update { it.copy(username = action.username, usernameError = null) }

            is SignUpAction.OnPasswordChange ->
                _state.update { it.copy(password = action.password, passwordError = null) }

            SignUpAction.OnSignUpClick -> signUp()

            SignUpAction.OnLoginClick ->
                viewModelScope.launch { eventChannel.send(SignUpEvent.NavigateToLogin) }
        }
    }

    private fun signUp() {
        val current = _state.value
        if (current.isLoading || !validate()) return

        val fullName = UserDataValidator.normalizeFullName(current.fullName)
        val username = UserDataValidator.normalizeUsername(current.username)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // TODO: real sign-up call with `username` + current.password.
                // If the backend says the username exists, show it on the field:
                //   _state.update { it.copy(usernameError = "Username is already taken") }
                //   return@launch   (the finally block still resets isLoading)
                delay(1_500)
                eventChannel.send(SignUpEvent.NavigateToHome)
            } catch (e: CancellationException) {
                throw e // never swallow cancellation
            } catch (e: Exception) {
                eventChannel.send(SignUpEvent.ShowError(e.message ?: "Something went wrong"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validate(): Boolean {
        val s = _state.value
        val fullNameError = UserDataValidator.fullNameError(s.fullName)
        val usernameError = UserDataValidator.usernameError(s.username)
        val passwordError = UserDataValidator.passwordError(s.password)
        _state.update {
            it.copy(
                fullNameError = fullNameError,
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }
        return fullNameError == null && usernameError == null && passwordError == null
    }
}