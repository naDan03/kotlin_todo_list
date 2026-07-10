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
            // On laisse le système gérer la transparence grâce à enableEdgeToEdge()
            // Mais on s'assure que les icônes sont de la bonne couleur.
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            // On met des icônes sombres par défaut pour le fond Slate50
            // Sauf si on est sur un écran avec le header bleu (on peut gérer ça par écran)
            insetsController.isAppearanceLightStatusBars = true 
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
