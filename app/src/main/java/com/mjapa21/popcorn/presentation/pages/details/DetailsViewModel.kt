package com.mjapa21.popcorn.presentation.pages.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjapa21.popcorn.domain.model.WatchStatus
import com.mjapa21.popcorn.domain.model.toMovie
import com.mjapa21.popcorn.domain.usecase.library.ObserveLibraryUseCase
import com.mjapa21.popcorn.domain.usecase.library.RemoveMovieUseCase
import com.mjapa21.popcorn.domain.usecase.library.SaveMovieUseCase
import com.mjapa21.popcorn.domain.usecase.library.UpdateMovieStatusUseCase
import com.mjapa21.popcorn.domain.usecase.movie.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val imdbId: String,
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val observeLibrary: ObserveLibraryUseCase,
    private val saveMovie: SaveMovieUseCase,
    private val updateStatus: UpdateMovieStatusUseCase,
    private val removeMovie: RemoveMovieUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    init {
        load()
        viewModelScope.launch {
            observeLibrary().collect { library ->
                val status = library.firstOrNull { it.id == imdbId }?.status
                _state.update { it.copy(status = status) }
            }
        }
    }


    fun onAction(action: DetailsAction) {
        when (action) {
            DetailsAction.Retry -> load()
            DetailsAction.AddToWatchlist -> save(WatchStatus.WATCHLIST)
            DetailsAction.MarkWatched ->
                if (_state.value.status == null) save(WatchStatus.WATCHED)
                else viewModelScope.launch { updateStatus(imdbId, WatchStatus.WATCHED) }

            DetailsAction.MoveToWatchlist ->
                viewModelScope.launch { updateStatus(imdbId, WatchStatus.WATCHLIST) }

            DetailsAction.Remove -> viewModelScope.launch { removeMovie(imdbId) }
        }
    }

    private fun save(status: WatchStatus) {
        val details = _state.value.details ?: return
        viewModelScope.launch { saveMovie(details.toMovie(), status) }
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