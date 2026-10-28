package com.mjapa21.popcorn.presentation.pages.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val imdbId: String,
    private val getMovieDetails: GetMovieDetailsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            DetailsAction.Retry -> load()

            // TODO: implement watchlist/watched feature
            DetailsAction.AddToWatchlist,
            DetailsAction.MarkWatched,
            DetailsAction.MoveToWatchlist,
            DetailsAction.Remove -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getMovieDetails(imdbId)
                .onSuccess { details ->
                    _state.update { it.copy(isLoading = false, details = details) }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, error = e.message ?: "Something went wrong")
                    }
                }
        }
    }
}