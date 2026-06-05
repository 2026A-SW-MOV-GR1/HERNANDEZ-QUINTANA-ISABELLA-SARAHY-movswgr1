package com.epn.mockcrud.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PastelBlue = Color(0xFFB3E5FC)
private val PastelPink = Color(0xFFF8BBD0)
private val PastelGreen = Color(0xFFC8E6C9)
private val PastelYellow = Color(0xFFFFF9C4)
private val PastelPurple = Color(0xFFE1BEE7)
private val PastelPeach = Color(0xFFFFCCBC)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFFF48FB1),
    tertiary = Color(0xFFA5D6A7),
    background = Color(0xFFFDFDFD),
    surface = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE3F2FD),
    secondaryContainer = Color(0xFFFCE4EC),
    onPrimaryContainer = Color(0xFF1976D2),
    onSecondaryContainer = Color(0xFFC2185B)
)

@Composable
fun AestheticTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Para simplificar y mantener el estilo pastel, usaremos mayormente el esquema claro
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}
