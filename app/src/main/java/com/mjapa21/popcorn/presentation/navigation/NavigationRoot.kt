package com.mjapa21.popcorn.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mjapa21.popcorn.presentation.pages.details.Details
import com.mjapa21.popcorn.presentation.pages.home.Home
import com.mjapa21.popcorn.presentation.pages.login.Login
import com.mjapa21.popcorn.presentation.pages.signup.SignUp

@Composable
fun NavigationRoot(modifier: Modifier = Modifier, startDestination: Destination) {
    val backStack = rememberNavBackStack(startDestination)

    fun navigateAsRoot(destination: Destination) {
        backStack.clear()
        backStack.add(destination)
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(), //this is needed, for example to avoid having the same viewmodel for all the details pages,
                // when navigating to a new details page, we want a new viewmodel instance
            ),
            onBack = { backStack.removeLastOrNull() },
            transitionSpec = {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            },
            popTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
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

                Destination.Home -> NavEntry(navKey) {
                    Home(onMovieClick = { imdbId ->
                        backStack.add(
                            Destination.Details(imdbId)
                        )
                    })
                }

                is Destination.Details -> NavEntry(navKey) {
                    Details(
                        imdbId = navKey.imdbId,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                else -> throw IllegalArgumentException("Unknown destination: $navKey")
            }
        }
    }
}