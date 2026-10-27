package com.mjapa21.popcorn.presentation.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val searchMovies: SearchMoviesUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onIntent(intent: HomeAction) {
        when (intent) {
            is HomeAction.QueryChanged -> onQueryChanged(intent.query)
            HomeAction.Search -> search()
        }
    }

    private fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query) }
        if (query.isBlank()) {
            searchJob?.cancel()
            _state.update { it.copy(isLoading = false, results = null, error = null) }
        }
    }

    private fun search() {
        val query = _state.value.query.trim()
        if (query.isEmpty()) return

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            searchMovies(query)
                .onSuccess { result ->
                    _state.update { it.copy(isLoading = false, results = result.movies) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            results = null,
                            error = e.message ?: "Something went wrong"
                        )
                    }
                }
        }
    }
}