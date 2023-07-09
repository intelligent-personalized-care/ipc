package pt.ipc_app.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.TextFieldType

@Composable
fun SearchMonitorsScreen(
    monitors: List<MonitorOutput>,
    requestState: ProgressState,
    onSearchRequest: (String) -> Unit = { },
    onMonitorClick: (MonitorOutput) -> Unit = { },
) {
    var typedUsername by remember { mutableStateOf(value = "") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
    ) {

        Text(
            text = stringResource(id = R.string.search_monitors),
            style = MaterialTheme.typography.h4,
        )

        CustomTextField(
            fieldType = TextFieldType.SEARCH,
            textToDisplay = typedUsername,
            updateText = { typedUsername = it },
            iconImageVector = Icons.Default.Search
        )

        CircularButton(
            icon = Icons.Default.Search,
            isEnabled = requestState != ProgressState.WAITING,
            state = requestState,
            onClick = { onSearchRequest(typedUsername) }
        )

        Spacer(modifier = Modifier.padding(top = 10.dp))

        if (monitors.isNotEmpty())
            MonitorsTable(
                monitors = monitors,
                onMonitorClick = onMonitorClick
            )
    }
}