package com.mjapa21.popcorn.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mjapa21.design_system.theme.PopcornTheme
import com.mjapa21.popcorn.presentation.navigation.Destination
import com.mjapa21.popcorn.presentation.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PopcornTheme {
                NavigationRoot(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = Destination.Login
                )
            }
        }
    }
}