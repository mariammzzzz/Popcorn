package com.mjapa21.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PopcornTitleHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    tags: List<String> = emptyList(),
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = colors.onBackground)
        if (!subtitle.isNullOrBlank()) {
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant
            )
        }
        if (tags.isNotEmpty()) {
            Text(
                text = tags.joinToString(" · "),
                style = MaterialTheme.typography.labelLarge,
                color = colors.onPrimaryContainer,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PopcornTitleHeaderPreview() {
    PopcornTitleHeader(
        title = "The Matrix",
        subtitle = "1999 · 2h 16m",
        tags = listOf("Action", "Sci-Fi", "Thriller"),
    )
}