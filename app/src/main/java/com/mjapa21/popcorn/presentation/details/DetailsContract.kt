package com.mjapa21.popcorn.presentation.details

import com.mjapa21.popcorn.domain.model.MovieDetails
import com.mjapa21.popcorn.domain.model.WatchStatus

data class DetailsState(
    val isLoading: Boolean = true,
    val details: MovieDetails? = null,
    val error: String? = null,
    val status: WatchStatus? = null, // null = not saved
)

sealed interface DetailsAction {
    data object Retry : DetailsAction
    data object AddToWatchlist : DetailsAction
    data object MarkWatched : DetailsAction
    data object MoveToWatchlist : DetailsAction
    data object Remove : DetailsAction
}