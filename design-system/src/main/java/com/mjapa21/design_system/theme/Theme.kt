package com.mjapa21.design_system.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Peach80,
    onPrimary = OnPeach80,
    primaryContainer = PeachContainer80,
    onPrimaryContainer = OnPeachContainer80,
    secondary = Lavender80,
    onSecondary = OnLavender80,
    secondaryContainer = LavenderContainer80,
    onSecondaryContainer = OnLavenderContainer80,
    tertiary = Mint80,
    error = Rose80,
    onError = OnRose80,
    background = Dusk80,
    onBackground = Lilac80,
    surface = DuskSurface80,
    onSurface = Lilac80,
    surfaceVariant = DuskSurfaceHigh80,
    onSurfaceVariant = Outline80,
    outline = Outline80,
    outlineVariant = OutlineVariant80,
)

private val LightColorScheme = lightColorScheme(
    primary = Peach40,
    onPrimary = OnPeach40,
    primaryContainer = PeachContainer40,
    onPrimaryContainer = OnPeachContainer40,
    secondary = Lavender40,
    onSecondary = OnLavender40,
    secondaryContainer = LavenderContainer40,
    onSecondaryContainer = OnLavenderContainer40,
    tertiary = Mint40,
    error = Rose40,
    onError = Color.White,
    background = Blush40,
    onBackground = Plum40,
    surface = Blush40,
    onSurface = Plum40,
    surfaceVariant = BlushSurfaceHigh40,
    onSurfaceVariant = Outline40,
    outline = Outline40,
    outlineVariant = OutlineVariant40,
)

@Composable
fun PopcornTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default so the brand palette isn't replaced by wallpaper colors.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}