package com.mjapa21.popcorn.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mjapa21.popcorn.presentation.pages.Login

@Composable
fun NavigationRoot(modifier: Modifier = Modifier, startDestination: Destination) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        modifier = modifier,
        backStack = backStack
    ) { navKey ->
        when (navKey) {
            Destination.Login -> NavEntry(navKey) { Login(onLoginSuccess = {}, onSignUpClick = {}) } //todo fix this
            Destination.SignUp -> NavEntry(navKey) { /* TODO: Implement SignUp screen */ }
            Destination.Home -> NavEntry(navKey) { /* TODO: Implement Home screen */ }
            else -> throw IllegalArgumentException("Unknown destination: $navKey")
        }
    }
}