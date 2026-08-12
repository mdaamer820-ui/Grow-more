package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkTypographyColorScheme = darkColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    primaryContainer = BrandPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = BrandPurple,
    onSecondary = Color.White,
    tertiary = AmberGold,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = LossRed
)

@Composable
fun GrowMoreTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkTypographyColorScheme,
        typography = Typography,
        content = content
    )
}



