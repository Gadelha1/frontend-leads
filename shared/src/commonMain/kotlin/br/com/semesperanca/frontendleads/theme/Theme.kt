package br.com.semesperanca.frontendleads.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0D6AA5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCFE6FF),
    onPrimaryContainer = Color(0xFF04263A),
    secondary = Color(0xFF3B6477),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBEE9FF),
    onSecondaryContainer = Color(0xFF001F2C),
    background = Color(0xFFF8F9FB),
    onBackground = Color(0xFF1C1C1E),
    surface = Color(0xFFF8F9FB),
    onSurface = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFFE7EAEE),
    onSurfaceVariant = Color(0xFF44484E),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
)

@Composable
fun MobileLeadsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content,
    )
}