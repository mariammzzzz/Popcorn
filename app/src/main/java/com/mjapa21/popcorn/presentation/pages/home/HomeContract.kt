package com.mjapa21.popcorn.presentation.pages.home

import com.mjapa21.popcorn.domain.model.Movie
import com.mjapa21.popcorn.domain.model.SavedMovie

data class HomeState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie>? = null,
    val error: String? = null,
    val watchlist: List<SavedMovie> = emptyList(),   //todo filled by Firestore later
    val watched: List<SavedMovie> = emptyList(),
) {
    val isSearching: Boolean get() = query.isNotBlank()
}

sealed interface HomeAction {
    data class QueryChanged(val query: String) : HomeAction
    data object Search : HomeAction
}