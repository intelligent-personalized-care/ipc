package pt.ipc_app.ui.screens.info

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.service.models.users.MonitorOutput

@Composable
fun MonitorDetailsScreen(
    monitor: MonitorOutput,
    onSendEmailRequest: () -> Unit = { }
) {
    Text(monitor.name)
    Button(onClick = onSendEmailRequest) {
        Text(monitor.email)
    }
}

@Preview
@Composable
fun MonitorDetailsScreenPreview() {

}