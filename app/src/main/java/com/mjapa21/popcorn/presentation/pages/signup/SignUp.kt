package com.mjapa21.popcorn.presentation.pages.signup

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mjapa21.design_system.components.PopcornButton
import com.mjapa21.design_system.components.PopcornButtonSize
import com.mjapa21.design_system.components.PopcornButtonStyle
import com.mjapa21.design_system.components.PopcornPasswordField
import com.mjapa21.design_system.components.PopcornTextField
import com.mjapa21.design_system.theme.PopcornTheme
import com.mjapa21.popcorn.domain.validation.UserDataValidator

@Composable
fun SignUp(
    onSignUpSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: SignUpViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                SignUpEvent.NavigateToHome -> onSignUpSuccess()
                SignUpEvent.NavigateToLogin -> onLoginClick()
                is SignUpEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    SignUpScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
    )
}
@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing), // status bar + nav bar + keyboard
        ) {
            //THIS IS THE SCROLLABLE PART that takes all the space the bottom section doesn't need
            //cause it has weight 1 while the bottom section has no weight
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Create your account",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Pick a username and password to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(32.dp))

                PopcornTextField(
                    value = state.fullName,
                    onValueChange = { onAction(SignUpAction.OnFullNameChange(it)) },
                    label = "Full name",
                    placeholder = "Jane Doe",
                    errorText = state.fullNameError,
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(20.dp))

                PopcornTextField(
                    value = state.username,
                    onValueChange = { onAction(SignUpAction.OnUsernameChange(it)) },
                    label = "Username",
                    placeholder = "your_username",
                    supportingText = "${UserDataValidator.USERNAME_MIN}–${UserDataValidator.USERNAME_MAX} characters: letters, numbers, underscores",
                    errorText = state.usernameError,
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
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
                    onValueChange = { onAction(SignUpAction.OnPasswordChange(it)) },
                    placeholder = "Create a password",
                    supportingText = "At least ${UserDataValidator.PASSWORD_MIN} characters, no spaces",
                    errorText = state.passwordError,
                    enabled = !state.isLoading,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (state.canSubmit) onAction(SignUpAction.OnSignUpClick)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // THIS IS PINNED SECTION: always visible and always on top of the keyboard
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PopcornButton(
                    text = if (state.isLoading) "Creating account…" else "Sign up",
                    onClick = { onAction(SignUpAction.OnSignUpClick) },
                    enabled = state.canSubmit,
                    loading = state.isLoading,
                    size = PopcornButtonSize.Large,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    PopcornButton(
                        text = "Log in",
                        onClick = { onAction(SignUpAction.OnLoginClick) },
                        style = PopcornButtonStyle.Text,
                        size = PopcornButtonSize.Small,
                        enabled = !state.isLoading,
                    )
                }
            }
        }
    }
}

@Preview(name = "Dark", showBackground = true, backgroundColor = 0xFF17131C)
@Composable
private fun SignUpScreenDarkPreview() = PopcornTheme(darkTheme = true) {
    SignUpScreen(
        state = SignUpState(username = "mjapa21", password = "secret1234"),
        onAction = {},
    )
}

@Preview(name = "Light", showBackground = true, backgroundColor = 0xFFFFF9F7)
@Composable
private fun SignUpScreenLightPreview() = PopcornTheme(darkTheme = false) {
    SignUpScreen(
        state = SignUpState(
            username = "mj",
            usernameError = "Username is already taken",
            fullName = "Mariam Japara"
        ),
        onAction = {},
    )
}