package com.nadan.firstappjetpackcompose.ui.theme

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = Slate400,
    tertiary = Slate300,
    background = Slate950,
    surface = Slate900,
    onPrimary = Slate950,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = Slate600,
    tertiary = Slate500,
    background = Slate50,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Slate900,
    onSurface = Slate900,
    outline = Slate300,
    outlineVariant = Slate200
)

@Composable
fun FirstAppJetPackComposeTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // On utilise une couleur solide pour que la barre reste bien visible
            window.statusBarColor = AccentBlue.toArgb()
            window.navigationBarColor = AccentBlue.toArgb()
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            // Icônes blanches sur fond sombre (AccentBlue)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
