package pt.ipc_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.utils.ProblemJson
import java.net.URI

@Composable
fun ErrorAlert(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        buttons = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                OutlinedButton(
                    border = BorderStroke(0.dp, Color.Unspecified),
                    onClick = { onDismiss() }
                ) {
                    Text(text = buttonText)
                }
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message) }
    )

}

@Composable
fun CheckProblemJson(error: ProblemJson?) {
    if (error != null) {
        var showDialog by remember { mutableStateOf(true) }

        if (showDialog) {
            ErrorAlert(
                title = error.title,
                message = "Status ${error.status}",
                buttonText = "OK",
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorAlertPreview() {
    ErrorAlert(
        title = "Error accessing server",
        message = "Could not ...",
        buttonText = "OK",
        onDismiss = { }
    )
}

@Preview(showBackground = true)
@Composable
fun CheckProblemJsonPreview() {
    CheckProblemJson(
        error = ProblemJson(
            type = URI("user-not-found"),
            title = "User Not Found",
            status = 404
        )
    )
}

