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
            is SignUpAction.OnEmailChange ->
                _state.update {
                    it.copy(
                        email = UserDataValidator.sanitizeEmail(action.email),
                        emailError = null,
                    )
                }

            is SignUpAction.OnPasswordChange ->
                _state.update {
                    it.copy(
                        password = UserDataValidator.sanitizePassword(action.password),
                        passwordError = null,
                    )
                }

            SignUpAction.OnSignUpClick -> signUp()

            SignUpAction.OnLoginClick ->
                viewModelScope.launch { eventChannel.send(SignUpEvent.NavigateToLogin) }
        }
    }

    private fun signUp() {
        val current = _state.value
        if (current.isLoading || !validate()) return

        val email = UserDataValidator.normalizeEmail(current.email)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // TODO: real sign-up call with `email` + current.password.
                // If the backend says the email exists, show it on the field:
                //   _state.update { it.copy(emailError = "Email is already in use") }
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
        val emailError = UserDataValidator.emailError(s.email)
        val passwordError = UserDataValidator.passwordError(s.password)
        _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
        return emailError == null && passwordError == null
    }
}