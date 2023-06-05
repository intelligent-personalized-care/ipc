package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.BottomBar
import pt.ipc_app.ui.components.ClientsTable
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo,
    clientsOfMonitor: List<ClientOutput>,
    onClientSelected: (ClientOutput) -> Unit = { },
    onHomeClick: () -> Unit = { },
    onPlansRequest: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {

    AppScreen {
        Row(
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hello) + " ${monitor.name}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.End
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClientsTable(
                columnText = stringResource(id = R.string.my_clients),
                clients = clientsOfMonitor,
                onClientClick = { onClientSelected(it) }
            )
        }

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                onHomeClick = onHomeClick,
                onExercisesClick = onPlansRequest,
                onUserInfoClick = onUserInfoClick,
                onAboutClick = onAboutClick
            )
        }

    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo(UUID.randomUUID(), "Test", "", Role.MONITOR),
        clientsOfMonitor = listOf(ClientOutput(UUID.randomUUID(), "Tiago", ""))
    )
}