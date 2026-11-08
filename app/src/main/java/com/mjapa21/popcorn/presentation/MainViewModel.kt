package com.mjapa21.popcorn.presentation

import androidx.lifecycle.ViewModel
import com.mjapa21.popcorn.domain.usecase.authetication.IsUserLoggedInUseCase
import com.mjapa21.popcorn.presentation.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainState(
    val startDestination: Destination? = null // null = still deciding
)

class MainViewModel(
    isUserLoggedIn: IsUserLoggedInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        _state.value = MainState(
            startDestination = if (isUserLoggedIn()) Destination.Home else Destination.Login
        )
    }
}