package com.mjapa21.popcorn.presentation.pages.home

import com.mjapa21.popcorn.domain.model.Movie

data class HomeState(
    val query: String = "",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,
)

sealed interface HomeAction {
    data class QueryChanged(val query: String) : HomeAction
    data object Search : HomeAction
}