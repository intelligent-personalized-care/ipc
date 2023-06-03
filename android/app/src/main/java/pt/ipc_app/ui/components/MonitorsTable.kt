package pt.ipc_app.ui.components

import androidx.compose.runtime.Composable
import pt.ipc_app.service.models.users.MonitorOutput

@Composable
fun MonitorsTable(
    monitors: List<MonitorOutput>,
    onMonitorClick: (MonitorOutput) -> Unit = { }
) {
    monitors.forEach {
        MonitorRow(
            monitor = it,
            onMonitorClick = { onMonitorClick(it) }
        )
    }
}
