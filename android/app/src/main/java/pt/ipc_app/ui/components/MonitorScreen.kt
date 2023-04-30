package pt.ipc_app.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pt.ipc_app.domain.user.Monitor

@Composable
fun MonitorScreen(
    monitor: Monitor
) {

    Text(monitor.name)

}