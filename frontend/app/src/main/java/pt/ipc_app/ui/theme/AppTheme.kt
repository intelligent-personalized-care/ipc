package pt.ipc_app.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Create a [AppTheme] that is based on the system's current theme.
 *
 * @param darkTheme whether the theme should be dark or light
 * @param content the content to be displayed
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private val DarkColorPalette = darkColors(
    primary = LightBlue,
    primaryVariant = DarkBlue,
    secondary = LightLightBlue
)

private val LightColorPalette = lightColors(
    primary = MediumBlue,
    primaryVariant = DarkBlue,
    secondary = LightLightBlue
)
