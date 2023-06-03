package pt.ipc_app.ui.screens.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun MonitorDetailsScreen(
    monitor: MonitorOutput,
    requestEnable: Boolean = true,
    onSendEmailRequest: () -> Unit = { },
    onRequestedConnection: () -> Unit = { }
) {
    AppScreen {
        Column {
            Text(monitor.name)
            Button(onClick = onSendEmailRequest) {
                Text(monitor.email)
            }
            if (requestEnable) {
                Button(
                    onClick = { onRequestedConnection() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(14, 145, 14, 255)),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Request",
                        tint = Color.White
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun MonitorDetailsScreenPreview() {
    MonitorDetailsScreen(monitor = MonitorOutput(UUID.randomUUID(), "Mike", "", 4.8F))
}