package pt.ipc_app.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pt.ipc_app.R
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.components.ClientsTable
import pt.ipc_app.ui.components.MonitorsTable
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun SearchMonitorsScreen(
    monitors: List<MonitorOutput>,
    requestState: ProgressState,
    onMonitorClick: (MonitorOutput) -> Unit = { },
) {
    AppScreen {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.search_results),
                style = MaterialTheme.typography.h4,
            )

            if (requestState == ProgressState.WAITING)
                CircularProgressIndicator()

            MonitorsTable(
                monitors = monitors,
                onMonitorClick = onMonitorClick
            )
        }
    }
}