package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularButton(
    icon: ImageVector,
    isEnabled: Boolean = true,
    state: ProgressState = ProgressState.IDLE,
    onClick: () -> Unit = { }
) {
    val size = 56.dp

    if (state == ProgressState.WAITING) {
        CircularProgressIndicator(
            Modifier.defaultMinSize(minWidth = size, minHeight = size)
        )
    }
    else {
        Button(
            onClick = onClick,
            enabled = isEnabled,
            shape = CircleShape,
            modifier = Modifier
                .defaultMinSize(minWidth = size, minHeight = size)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Circular Button"
            )
        }
    }
}

@Preview
@Composable
fun AuthenticationButtonPreview() {
    CircularButton(
        icon = Icons.Default.Login
    )
}