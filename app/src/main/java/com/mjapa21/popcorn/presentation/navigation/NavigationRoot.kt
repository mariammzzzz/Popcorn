package com.mjapa21.popcorn.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.mjapa21.popcorn.presentation.pages.home.Home
import com.mjapa21.popcorn.presentation.pages.login.Login
import com.mjapa21.popcorn.presentation.pages.signup.SignUp

@Composable
fun NavigationRoot(modifier: Modifier = Modifier, startDestination: Destination) {
    val backStack = rememberNavBackStack(startDestination)


    /** Makes [destination] the only screen: nothing underneath, so Back exits the app. */
    fun navigateAsRoot(destination: Destination) {
        backStack.clear()
        backStack.add(destination)
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
    ) { navKey ->
        when (navKey) {
            Destination.Login -> NavEntry(navKey) {
                Login(
                    onLoginSuccess = { navigateAsRoot(Destination.Home) },
                    onSignUpClick = { backStack.add(Destination.SignUp) },
                )
            }

            Destination.SignUp -> NavEntry(navKey) {
                SignUp(
                    onSignUpSuccess = { navigateAsRoot(Destination.Home) },
                    onLoginClick = { backStack.removeLastOrNull() },
                )
            }

            Destination.Home -> NavEntry(navKey) { Home() }
            else -> throw IllegalArgumentException("Unknown destination: $navKey")
        }
    }
}