package com.mjapa21.design_system.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mjapa21.design_system.R
import com.mjapa21.design_system.theme.PopcornTheme

enum class PopcornButtonStyle { Primary, Secondary, Outline, Text }

enum class PopcornButtonSize(val height: Dp, val horizontalPadding: Dp, val iconSize: Dp) {
    Small(height = 40.dp, horizontalPadding = 16.dp, iconSize = 18.dp),
    Medium(height = 52.dp, horizontalPadding = 24.dp, iconSize = 20.dp),
    Large(height = 60.dp, horizontalPadding = 28.dp, iconSize = 22.dp),
}

/**
 * Popcorn's main button.
 *
 * - [PopcornButtonStyle.Primary]: warm gradient + soft glow (use once per screen).
 * - [PopcornButtonStyle.Secondary]: tonal fill.
 * - [PopcornButtonStyle.Outline]: bordered, transparent.
 * - [PopcornButtonStyle.Text]: lowest emphasis.
 *
 * Press feedback is a springy scale; [loading] swaps in a spinner and ignores clicks
 * while keeping the button's normal (non-disabled) look.
 */
@Composable
fun PopcornButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PopcornButtonStyle = PopcornButtonStyle.Primary,
    size: PopcornButtonSize = PopcornButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    @DrawableRes leadingIcon: Int? = null,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled && !loading) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "popcorn-button-scale"
    )

    val contentColor = when {
        !enabled -> colors.onSurface.copy(alpha = 0.38f)
        else -> when (style) {
            PopcornButtonStyle.Primary -> colors.onPrimary
            PopcornButtonStyle.Secondary -> colors.onSecondaryContainer
            PopcornButtonStyle.Outline, PopcornButtonStyle.Text -> colors.onPrimaryContainer
        }
    }

    val containerBrush: Brush? = when {
        style == PopcornButtonStyle.Outline || style == PopcornButtonStyle.Text -> null
        !enabled -> SolidColor(colors.onSurface.copy(alpha = 0.12f))
        style == PopcornButtonStyle.Primary -> Brush.horizontalGradient(
            listOf(colors.primary, lerp(colors.primary, colors.secondary, 0.45f))
        )

        else -> SolidColor(colors.secondaryContainer)
    }

    val border = if (style == PopcornButtonStyle.Outline) {
        BorderStroke(
            width = 1.5.dp,
            color = if (enabled) colors.primary.copy(alpha = 0.6f)
            else colors.onSurface.copy(alpha = 0.12f)
        )
    } else null

    val glow = if (style == PopcornButtonStyle.Primary && enabled) {
        Modifier.shadow(
            elevation = 10.dp,
            shape = shape,
            ambientColor = colors.primary.copy(alpha = 0.25f),
            spotColor = colors.primary.copy(alpha = 0.5f),
        )
    } else Modifier

    Surface(
        onClick = { if (!loading) onClick() },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(glow),
        enabled = enabled,
        shape = shape,
        color = Color.Transparent,
        contentColor = contentColor,
        border = border,
        interactionSource = interactionSource,
    ) {
        Box(
            modifier = Modifier
                .then(if (containerBrush != null) Modifier.background(containerBrush) else Modifier)
                .defaultMinSize(minHeight = size.height)
                .padding(horizontal = size.horizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility(visible = loading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(size.iconSize),
                            strokeWidth = 2.dp,
                            color = contentColor,
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                }
                if (leadingIcon != null && !loading) {
                    Icon(
                        painter = painterResource(leadingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(size.iconSize),
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(text = text, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun PopcornButtonDarkPreview() = PopcornTheme(darkTheme = true) { ButtonShowcase() }

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFF9F7)
@Composable
private fun PopcornButtonLightPreview() = PopcornTheme(darkTheme = false) { ButtonShowcase() }

@Composable
private fun ButtonShowcase() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PopcornButton("Log in", {}, Modifier.fillMaxWidth(), size = PopcornButtonSize.Large)
        PopcornButton(
            "Save to Watchlist",
            {},
            Modifier.fillMaxWidth(),
            leadingIcon = R.drawable.outline_bookmark_star_24
        )
        PopcornButton("Signing in…", {}, Modifier.fillMaxWidth(), loading = true)
        PopcornButton(
            "Create account",
            {},
            Modifier.fillMaxWidth(),
            style = PopcornButtonStyle.Secondary
        )
        PopcornButton(
            "Continue as guest",
            {},
            Modifier.fillMaxWidth(),
            style = PopcornButtonStyle.Outline
        )
        PopcornButton(
            "Forgot password?",
            {},
            style = PopcornButtonStyle.Text,
            size = PopcornButtonSize.Small
        )
        PopcornButton("Disabled", {}, Modifier.fillMaxWidth(), enabled = false)
    }
}