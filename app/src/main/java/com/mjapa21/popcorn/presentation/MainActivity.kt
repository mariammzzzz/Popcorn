package com.mjapa21.popcorn.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mjapa21.design_system.theme.PopcornTheme
import com.mjapa21.popcorn.presentation.navigation.NavigationRoot
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            viewModel.state.value.startDestination == null
        }

        setContent {
            PopcornTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                state.startDestination?.let { destination ->
                    NavigationRoot(
                        modifier = Modifier.fillMaxSize(),
                        startDestination = destination
                    )
                }
            }
        }
    }
}