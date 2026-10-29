package com.mjapa21.popcorn.presentation.pages.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mjapa21.popcorn.domain.usecase.authetication.SignUpUseCase
import com.mjapa21.popcorn.domain.validation.UserDataValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

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
            signUpUseCase(email, current.password)
                .onSuccess {
                    if (firebaseAuth.currentUser != null) {
                        eventChannel.send(SignUpEvent.NavigateToHome)
                    } else {
                        eventChannel.send(SignUpEvent.NavigateToLogin)
                    }
                }
                .onFailure {
                    eventChannel.send(SignUpEvent.ShowError(it.message ?: "Unknown error"))
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