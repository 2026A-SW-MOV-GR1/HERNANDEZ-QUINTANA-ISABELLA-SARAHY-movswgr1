package com.example.aplicaciondisney

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DisneyBackground = Color(0xFF1A1D29)
val DisneySurface = Color(0xFF252833)
val DisneyPrimary = Color(0xFF0072D2)
val DisneyOnSurface = Color(0xFFF9F9F9)

private val DarkColorScheme = darkColorScheme(
    primary = DisneyPrimary,
    background = DisneyBackground,
    surface = DisneySurface,
    onBackground = DisneyOnSurface,
    onSurface = DisneyOnSurface
)

@Composable
fun DisneyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
