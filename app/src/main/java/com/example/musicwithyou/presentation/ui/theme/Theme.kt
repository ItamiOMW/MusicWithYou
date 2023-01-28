package com.example.musicwithyou.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


val DarkColorScheme = darkColors(
    primary = DarkBlue,
    onPrimary = White,
    secondary = White,
    onSecondary = DarkBlue,
    secondaryVariant = Gray,
    error = Pink,
    onError = White,
    background = DarkBlue,
    onBackground = White,
    surface = LightGray,
    onSurface = White
)

val LightColorScheme = lightColors(
    primary = White,
    onPrimary = DarkBlue,
    secondary = DarkBlue,
    onSecondary = White,
    secondaryVariant = Gray,
    error = Pink,
    onError = White,
    background = White,
    onBackground = DarkBlue,
    surface = LightGray,
    onSurface = DarkBlue,
    primaryVariant = White
)

@Composable
fun MusicWithYouTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colors = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colors = colors,
        typography = CustomTypography,
        shapes = Shapes,
        content = content
    )
}