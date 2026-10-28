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
fun PopcornInfoList(
    items: List<Pair<String, String?>>,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        items.forEach { (label, value) ->
            if (value != null) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurface
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PopcornInfoListPreview() {
    PopcornInfoList(
        items = listOf(
            "Release Date" to "2023-06-01",
            "Runtime" to "120 min",
            "Director" to "John Doe",
            "Genre" to null,
        )
    )
}