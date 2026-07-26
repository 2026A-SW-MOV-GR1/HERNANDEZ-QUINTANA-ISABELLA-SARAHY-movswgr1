package com.example.aplicaciondisney

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    DisneyTheme {
        Box {
            DisneyHome(onMovieClick = { movie ->
                selectedMovie = movie
            })

            if (selectedMovie != null) {
                MovieDetailScreen(
                    movie = selectedMovie!!,
                    onBack = { selectedMovie = null }
                )
            }
        }
    }
}
