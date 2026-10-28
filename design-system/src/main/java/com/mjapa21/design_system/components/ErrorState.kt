package com.mjapa21.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun PopcornErrorState(
    title: String,
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String = "😕",
    retryText: String = "Try again",
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PopcornEmptyState(emoji = emoji, title = title, message = message)
        PopcornButton(
            text = retryText,
            onClick = onRetry,
            style = PopcornButtonStyle.Secondary,
        )
    }
}