package com.mjapa21.popcorn.presentation.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val searchMovies: SearchMoviesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onIntent(intent: HomeAction) {
        when (intent) {
            is HomeAction.QueryChanged -> _state.update { it.copy(query = intent.query) }
            HomeAction.Search -> search()
        }
    }

    private fun search() {
        val query = _state.value.query.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            searchMovies(query)
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, movies = result.movies) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            movies = emptyList(),
                            error = e.message ?: "Something went wrong",
                        )
                    }
                }
        }
    }
}