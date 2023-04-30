package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.user.User

enum class ProgressState { Idle, Creating }

@Composable
fun RegisterButton(
    validationInfo: User?,
    state: ProgressState = ProgressState.Idle,
    onClick: (User) -> Unit = { }
) {
    Spacer(modifier = Modifier.height(5.dp))

    val size = 56.dp

    if (state == ProgressState.Creating) {
        CircularProgressIndicator(
            Modifier.defaultMinSize(minWidth = size, minHeight = size)
        )
    }
    else {
        var editing by remember { mutableStateOf(validationInfo == null) }

        Button(
            onClick = {
                if (!editing) editing = true
                else if (validationInfo != null) {
                    onClick(validationInfo)
                }
            },
            enabled = validationInfo != null,
            shape = CircleShape,
            modifier = Modifier
                .defaultMinSize(minWidth = size, minHeight = size)
        ) {
            Icon(imageVector = Icons.Default.Login, contentDescription = "")
        }
    }
}

@Preview
@Composable
fun RegisterButtonPreview() {
    RegisterButton(
        validationInfo = null,
        state = ProgressState.Creating
    )
}