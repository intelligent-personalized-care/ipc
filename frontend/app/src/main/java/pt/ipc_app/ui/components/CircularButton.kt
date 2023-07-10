package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CircularButton(
    icon: ImageVector,
    color: Color? = null,
    isEnabled: Boolean = true,
    state: ProgressState = ProgressState.IDLE,
    onClick: () -> Unit = { }
) {
    val size = 56.dp

    if (state == ProgressState.WAITING) {
        CircularProgressIndicator(
            modifier = Modifier.defaultMinSize(minWidth = size, minHeight = size),
            color = color ?: Color(27, 69, 113)
        )
    }
    else {
        Button(
            onClick = onClick,
            enabled = isEnabled,
            shape = CircleShape,
            modifier = Modifier
                .defaultMinSize(minWidth = size, minHeight = size),
            colors = ButtonDefaults.buttonColors(backgroundColor = color ?: Color(27, 69, 113), contentColor = Color.White)
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