package com.mjapa21.design_system.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mjapa21.design_system.theme.PopcornTheme

/**
 * Popcorn's text input: label on top, soft filled container, colored border on focus,
 * and an animated error / supporting message underneath.
 *
 * Pass [errorText] to put the field in the error state (it replaces [supportingText]).
 */
@Composable
fun PopcornTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    val isError = errorText != null
    val message = errorText ?: supportingText

    val leading: (@Composable () -> Unit)? = leadingIcon?.let { iconRes ->
        {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isError) colors.error else colors.onSurface,
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge,
            placeholder = placeholder?.let {
                { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            },
            leadingIcon = leading,
            trailingIcon = trailingContent,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                // container
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                disabledContainerColor = colors.surfaceVariant.copy(alpha = 0.5f),
                errorContainerColor = colors.surfaceVariant,
                // border: invisible until focused / error
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = colors.error,
                // text
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                disabledTextColor = colors.onSurface.copy(alpha = 0.38f),
                errorTextColor = colors.onSurface,
                cursorColor = colors.onPrimaryContainer,
                errorCursorColor = colors.error,
                // placeholder + icons
                focusedPlaceholderColor = colors.onSurfaceVariant.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = colors.onSurfaceVariant.copy(alpha = 0.7f),
                errorPlaceholderColor = colors.onSurfaceVariant.copy(alpha = 0.7f),
                focusedLeadingIconColor = colors.onPrimaryContainer,
                unfocusedLeadingIconColor = colors.onSurfaceVariant,
                errorLeadingIconColor = colors.error,
            ),
        )

        AnimatedVisibility(visible = message != null) {
            Text(
                text = message.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) colors.error else colors.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp),
            )
        }
    }
}

/**
 * [PopcornTextField] preconfigured for passwords, with a Show / Hide toggle.
 * Visibility is remembered across rotation.
 */
@Composable
fun PopcornPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var visible by rememberSaveable { mutableStateOf(false) }

    PopcornTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        supportingText = supportingText,
        errorText = errorText,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingContent = {
            Text(
                text = if (visible) "Hide" else "Show",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(role = Role.Button) { visible = !visible }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            )
        },
    )
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun PopcornTextFieldDarkPreview() = PopcornTheme(darkTheme = true) { FieldShowcase() }

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFF9F7)
@Composable
private fun PopcornTextFieldLightPreview() = PopcornTheme(darkTheme = false) { FieldShowcase() }

@Composable
private fun FieldShowcase() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("secret123") }
    var wrongEmail by remember { mutableStateOf("not-an-email") }

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        PopcornTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "you@example.com",
            supportingText = "We'll never share your email",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )
        PopcornPasswordField(value = password, onValueChange = { password = it })
        PopcornTextField(
            value = wrongEmail,
            onValueChange = { wrongEmail = it },
            label = "Email",
            errorText = "Enter a valid email address",
        )
        PopcornTextField(
            value = "",
            onValueChange = {},
            label = "Disabled",
            placeholder = "Can't type here",
            enabled = false,
        )
    }
}