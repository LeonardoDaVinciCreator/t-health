package com.tbank.t_health.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = White,
    primaryContainer = PrimaryBlue,
    onPrimaryContainer = White,

    secondary = SecondaryBlue,
    onSecondary = White,
    secondaryContainer = SecondaryBlue,
    onSecondaryContainer = White,

    tertiary = Gray400,
    onTertiary = Black,
    tertiaryContainer = AccentYellow,
    onTertiaryContainer = Black,

    background = Gray50,
    onBackground = Gray800,

    surface = White,
    onSurface = Gray800,

    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,

    error = ErrorRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = White,
    primaryContainer = DarkPrimary,
    onPrimaryContainer = White,

    secondary = SecondaryBlue,
    onSecondary = White,
    secondaryContainer = SecondaryBlue,
    onSecondaryContainer = White,

    tertiary = AccentYellow,
    onTertiary = Black,
    tertiaryContainer = AccentYellow,
    onTertiaryContainer = Black,

    background = DarkBackground,
    onBackground = White,

    surface = DarkSurface,
    onSurface = White,

    surfaceVariant = Gray800,
    onSurfaceVariant = Gray400,

    error = ErrorRed,
    onError = White
)

@Composable
fun THealthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // отключение dynamic colors для кастомного дизайна
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = THealthTypography,
        content = content
    )
}