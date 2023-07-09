package pt.ipc_app.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import pt.ipc_app.service.models.users.MonitorOutput

@Composable
fun MonitorsTable(
    monitors: List<MonitorOutput>,
    onMonitorClick: (MonitorOutput) -> Unit = { }
) {
    LazyColumn {

        items(monitors) {
            MonitorRow(
                monitor = it,
                onMonitorClick = { onMonitorClick(it) }
            )
        }
    }
}
