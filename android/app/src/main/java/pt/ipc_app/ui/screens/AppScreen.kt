package pt.ipc_app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.ipc_app.ui.theme.AppTheme

/**
 * A screen that displays the app.
 *
 * @param content the content to be displayed
 */
@Composable
fun AppScreen(content: @Composable () -> Unit) {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
            content = content
        )
    }
}