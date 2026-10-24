package com.mjapa21.popcorn.presentation.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjapa21.design_system.components.PopcornButton
import com.mjapa21.design_system.components.PopcornButtonSize
import com.mjapa21.design_system.components.PopcornButtonStyle
import com.mjapa21.design_system.components.PopcornPasswordField
import com.mjapa21.design_system.components.PopcornTextField
import com.mjapa21.design_system.theme.PopcornTheme


@Composable
fun Login(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
) {

    LoginScreen(
        state = viewModel.state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = {
            if (viewModel.validate()) {
                // TODO: call real auth, then navigate on success
                onLoginSuccess()
            }
        },
        onSignUpClick = onSignUpClick,
    )
}


@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0), // we handle insets ourselves below
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing) // status bar + nav bar + keyboard
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                //todo change this
                text = "Welcome back",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Log in to continue to Popcorn",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            PopcornTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = "Email",
                placeholder = "you@example.com",
                errorText = state.emailError,
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(20.dp))

            PopcornPasswordField(
                value = state.password,
                onValueChange = onPasswordChange,
                placeholder = "Enter your password",
                errorText = state.passwordError,
                enabled = !state.isLoading,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (state.canSubmit) onLoginClick()
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(28.dp))

            PopcornButton(
                text = if (state.isLoading) "Signing in…" else "Log in",
                onClick = onLoginClick,
                enabled = state.canSubmit,
                loading = state.isLoading,
                size = PopcornButtonSize.Large,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
                PopcornButton(
                    text = "Sign up",
                    onClick = onSignUpClick,
                    style = PopcornButtonStyle.Text,
                    size = PopcornButtonSize.Small,
                    enabled = !state.isLoading,
                )
            }
        }
    }
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun LoginScreenDarkPreview() = PopcornTheme(darkTheme = true) {
    LoginScreen(
        state = LoginUiState(email = "me@popcorn.com", password = "secret123"),
        onEmailChange = {}, onPasswordChange = {}, onLoginClick = {}, onSignUpClick = {},
    )
}

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFF9F7)
@Composable
private fun LoginScreenLightPreview() = PopcornTheme(darkTheme = false) {
    LoginScreen(
        state = LoginUiState(email = "bad-email", emailError = "Enter a valid email address"),
        onEmailChange = {}, onPasswordChange = {}, onLoginClick = {}, onSignUpClick = {},
    )
}