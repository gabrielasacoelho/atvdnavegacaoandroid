package com.example.diario

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Único lugar onde Color(0xFF...) aparece
private val DiarioColors = lightColorScheme(
    primary = Color(0xFFD84315),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFCCBC),
    onPrimaryContainer = Color(0xFF3E1000),
    secondary = Color(0xFF37474F),
    background = Color(0xFFFFF8F5),
    surface = Color(0xFFFFF8F5),
    onSurface = Color(0xFF1C1B1A)
)

@Composable
fun DiarioTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DiarioColors, content = content)
}
