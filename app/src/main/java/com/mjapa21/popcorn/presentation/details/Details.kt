package com.mjapa21.popcorn.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mjapa21.design_system.components.PopcornButton
import com.mjapa21.design_system.components.PopcornButtonStyle
import com.mjapa21.design_system.components.PopcornErrorState
import com.mjapa21.design_system.components.PopcornInfoList
import com.mjapa21.design_system.components.PopcornPoster
import com.mjapa21.design_system.components.PopcornRatingChip
import com.mjapa21.design_system.components.PopcornScreen
import com.mjapa21.design_system.components.PopcornSectionHeader
import com.mjapa21.design_system.components.PopcornTitleHeader
import com.mjapa21.design_system.theme.PopcornTheme
import com.mjapa21.popcorn.domain.model.MediaType
import com.mjapa21.popcorn.domain.model.MovieDetails
import com.mjapa21.popcorn.domain.model.Rating
import com.mjapa21.popcorn.domain.model.WatchStatus
import com.mjapa21.design_system.R as DesignSystemR
import com.mjapa21.popcorn.R as AppR

//@Composable
//fun DetailsRoot(
//    imdbId: String,
//    onBack: () -> Unit,
//    viewModel: DetailsViewModel = koinViewModel(parameters = { parametersOf(imdbId) }),
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//    DetailsScreen(
//        state = state,
//        onAction = viewModel::onAction,
//        onBack = onBack,
//    )
//}

@Composable
fun DetailsScreen(
    state: DetailsState,
    onAction: (DetailsAction) -> Unit,
    onBack: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme

    PopcornScreen {
        IconButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = painterResource(AppR.drawable.outline_chevron_backward_24),
                contentDescription = "Back",
                tint = colors.onBackground,
            )
        }

        val details = state.details
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = colors.primary)
            }

            details == null -> PopcornErrorState(
                title = "Couldn't load this title",
                message = state.error.orEmpty(),
                onRetry = { onAction(DetailsAction.Retry) },
            )

            else -> DetailsBody(details, state.status, onAction)
        }
    }
}

@Composable
private fun DetailsBody(
    details: MovieDetails,
    status: WatchStatus?,
    onAction: (DetailsAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PopcornPoster(
            posterUrl = details.posterUrl,
            contentDescription = details.title,
            modifier = Modifier
                .width(200.dp)
                .aspectRatio(2f / 3f)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(20.dp),
        )

        PopcornTitleHeader(
            title = details.title,
            subtitle = listOfNotNull(details.year, details.runtime, details.rated)
                .joinToString(" • "),
            tags = details.genres,
        )

        LibraryActions(status, onAction)

        if (details.ratings.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                details.ratings.forEach {
                    PopcornRatingChip(value = it.value, label = it.displayName())
                }
            }
        }

        details.plot?.let { plot ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PopcornSectionHeader("Plot")
                Text(
                    text = plot,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        PopcornInfoList(
            items = listOf(
                "Director" to details.director,
                "Writer" to details.writer,
                "Cast" to details.actors,
                "Released" to details.released,
                "Language" to details.language,
                "Country" to details.country,
                "Awards" to details.awards,
            ),
        )
    }
}

@Composable
private fun LibraryActions(status: WatchStatus?, onAction: (DetailsAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (status) {
            null -> {
                PopcornButton(
                    text = "Add to watchlist",
                    onClick = { onAction(DetailsAction.AddToWatchlist) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = DesignSystemR.drawable.outline_bookmark_star_24,
                )
                PopcornButton(
                    text = "Mark as watched",
                    onClick = { onAction(DetailsAction.MarkWatched) },
                    modifier = Modifier.fillMaxWidth(),
                    style = PopcornButtonStyle.Secondary,
                )
            }

            WatchStatus.WATCHLIST -> {
                PopcornButton(
                    text = "Mark as watched",
                    onClick = { onAction(DetailsAction.MarkWatched) },
                    modifier = Modifier.fillMaxWidth(),
                )
                PopcornButton(
                    text = "Remove from watchlist",
                    onClick = { onAction(DetailsAction.Remove) },
                    modifier = Modifier.fillMaxWidth(),
                    style = PopcornButtonStyle.Outline,
                )
            }

            WatchStatus.WATCHED -> {
                PopcornButton(
                    text = "Move back to watchlist",
                    onClick = { onAction(DetailsAction.MoveToWatchlist) },
                    modifier = Modifier.fillMaxWidth(),
                    style = PopcornButtonStyle.Secondary,
                )
                PopcornButton(
                    text = "Remove from watched",
                    onClick = { onAction(DetailsAction.Remove) },
                    modifier = Modifier.fillMaxWidth(),
                    style = PopcornButtonStyle.Outline,
                )
            }
        }
    }
}

private fun Rating.displayName() = when (source) {
    "Internet Movie Database" -> "IMDb"
    else -> source
}

// ---------------- Previews ----------------

private val previewDetails = MovieDetails(
    id = "tt7158582",
    title = "The Reports on Sarah and Saleem",
    year = "2018",
    type = MediaType.MOVIE,
    posterUrl = null, // coil wont load in preview
    rated = null,
    released = "12 Jun 2019",
    runtime = "127 min",
    genres = listOf("Crime", "Drama", "Mystery"),
    director = "Muayad Alayan",
    writer = "Rami Musa Alayan",
    actors = "Adeeb Safadi, Sivane Kretchner, Ishai Golan",
    plot = "The affair of a married Palestinian man and a married Israeli woman in Jerusalem " +
            "takes a dangerous political dimension when they are spotted in the wrong place " +
            "at the wrong time.",
    language = "Arabic, Hebrew, English",
    country = "Occupied Palestinian Territory, Germany, Netherlands, Mexico",
    awards = "7 wins & 9 nominations",
    ratings = listOf(
        Rating("Internet Movie Database", "7.2/10"),
        Rating("Rotten Tomatoes", "94%"),
        Rating("Metacritic", "69/100"),
    ),
)

@Composable
private fun PreviewHost(state: DetailsState, dark: Boolean) {
    PopcornTheme(darkTheme = dark) {
        DetailsScreen(state = state, onAction = {}, onBack = {})
    }
}

@Preview(
    name = "Not saved - Dark",
    showBackground = true,
    backgroundColor = 0xFF17131C,
    heightDp = 1100
)
@Composable
private fun DetailsNotSavedDarkPreview() = PreviewHost(
    DetailsState(isLoading = false, details = previewDetails, status = null), dark = true,
)

@Preview(
    name = "Not saved - Light",
    showBackground = true,
    backgroundColor = 0xFFFFF9F7,
    heightDp = 1100
)
@Composable
private fun DetailsNotSavedLightPreview() = PreviewHost(
    DetailsState(isLoading = false, details = previewDetails, status = null), dark = false,
)

@Preview(
    name = "Watchlist - Dark",
    showBackground = true,
    backgroundColor = 0xFF17131C,
    heightDp = 1100
)
@Composable
private fun DetailsWatchlistPreview() = PreviewHost(
    DetailsState(isLoading = false, details = previewDetails, status = WatchStatus.WATCHLIST),
    dark = true,
)

@Preview(
    name = "Watched - Dark",
    showBackground = true,
    backgroundColor = 0xFF17131C,
    heightDp = 1100
)
@Composable
private fun DetailsWatchedPreview() = PreviewHost(
    DetailsState(isLoading = false, details = previewDetails, status = WatchStatus.WATCHED),
    dark = true,
)

@Preview(name = "Loading - Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun DetailsLoadingPreview() = PreviewHost(DetailsState(isLoading = true), dark = true)

@Preview(name = "Error - Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun DetailsErrorPreview() = PreviewHost(
    DetailsState(isLoading = false, error = "Error getting data."), dark = true,
)