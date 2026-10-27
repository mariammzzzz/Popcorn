package com.mjapa21.popcorn.presentation.pages.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mjapa21.design_system.components.PopcornEmptyState
import com.mjapa21.design_system.components.PopcornMovieListItem
import com.mjapa21.design_system.components.PopcornPosterCard
import com.mjapa21.design_system.components.PopcornSearchBar
import com.mjapa21.design_system.components.PopcornSectionHeader
import com.mjapa21.popcorn.domain.model.SavedMovie
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Home(
    viewModel: HomeViewModel = koinViewModel(),
    onMovieClick: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeContent(state, viewModel::onIntent, onMovieClick)
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeAction) -> Unit,
    onMovieClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 16.dp),
    ) {
        Text(
            text = "What are we watching?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(Modifier.height(16.dp))
        PopcornSearchBar(
            query = state.query,
            onQueryChange = { onIntent(HomeAction.QueryChanged(it)) },
            onSearch = { onIntent(HomeAction.Search) },
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(Modifier.height(8.dp))

        Crossfade(targetState = state.isSearching, label = "home-mode") { searching ->
            if (searching) {
                SearchContent(state, onMovieClick)
            } else {
                LibraryContent(state.watchlist, state.watched, onMovieClick)
            }
        }
    }
}

@Composable
private fun LibraryContent(
    watchlist: List<SavedMovie>,
    watched: List<SavedMovie>,
    onMovieClick: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            PosterSection(
                title = "Watchlist",
                movies = watchlist,
                emptyEmoji = "🎬",
                emptyTitle = "Nothing queued up",
                emptyMessage = "Search for a movie and save it to watch later.",
                onMovieClick = onMovieClick,
            )
        }
        item {
            PosterSection(
                title = "Watched",
                movies = watched,
                emptyEmoji = "🍿",
                emptyTitle = "No movies watched yet",
                emptyMessage = "Movies you finish will show up here.",
                onMovieClick = onMovieClick,
            )
        }
    }
}

@Composable
private fun PosterSection(
    title: String,
    movies: List<SavedMovie>,
    emptyEmoji: String,
    emptyTitle: String,
    emptyMessage: String,
    onMovieClick: (String) -> Unit,
) {
    Column {
        PopcornSectionHeader(title, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(Modifier.height(12.dp))
        if (movies.isEmpty()) {
            PopcornEmptyState(emptyEmoji, emptyTitle, emptyMessage)
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(movies, key = { it.id }) { movie ->
                    PopcornPosterCard(
                        title = movie.title,
                        posterUrl = movie.posterUrl,
                        subtitle = movie.year,
                        onClick = { onMovieClick(movie.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchContent(state: HomeState, onMovieClick: (String) -> Unit) {
    val results = state.results
    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> PopcornEmptyState("😕", "Couldn't search", state.error)
        results == null -> PopcornEmptyState(
            "🔎",
            "Ready when you are",
            "Type a title and hit search."
        )

        results.isEmpty() -> PopcornEmptyState("🤷", "No matches", "Try a different title.")
        else -> LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(results, key = { it.id }) { movie ->
                PopcornMovieListItem(
                    title = movie.title,
                    subtitle = "${movie.year} • ${
                        movie.type.name.lowercase().replaceFirstChar { it.uppercase() }
                    }",
                    posterUrl = movie.posterUrl,
                    onClick = { onMovieClick(movie.id) },
                )
            }
        }
    }
}