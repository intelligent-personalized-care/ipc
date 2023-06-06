package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.session.UserInfo
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestInformation
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ClientsTable
import pt.ipc_app.ui.components.NotificationIcon
import pt.ipc_app.ui.components.bottomBar.MonitorBottomBar
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo,
    clientsOfMonitor: List<ClientOutput>,
    requestsOfMonitor: List<RequestInformation>,
    onClientSelected: (ClientOutput) -> Unit = { },
    onClientRequestAccepted: (RequestInformation, ConnectionRequestDecisionInput) -> Unit = { _,_ -> },
    onPlanCreateClick: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    var notifications by remember { mutableStateOf(true) }

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

        NotificationIcon(
            notifications = notifications,
            onClick = { if (notifications) notifications = false }
        )

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 130.dp)
        ) {
            ClientsTable(
                columnText = stringResource(id = R.string.client_requests),
                icon = Icons.Default.Add,
                clients = requestsOfMonitor.map { ClientOutput(it.clientID, it.clientName, it.clientEmail) },
                onClientClick = { client ->
                    onClientRequestAccepted(
                        requestsOfMonitor.first { it.clientID == client.id },
                        ConnectionRequestDecisionInput(true)
                    )
                },
                modifier = Modifier.height(200.dp)
            )

            ClientsTable(
                columnText = stringResource(id = R.string.my_clients),
                clients = clientsOfMonitor,
                onClientClick = { onClientSelected(it) }
            )
        }


        MonitorBottomBar(
            onPlanCreateClick = onPlanCreateClick,
            onUserInfoClick = onUserInfoClick,
            onAboutClick = onAboutClick
        )
    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo(UUID.randomUUID().toString(), "Test", "", Role.MONITOR),
        clientsOfMonitor = listOf(ClientOutput(UUID.randomUUID(), "Tiago", "")),
        requestsOfMonitor = listOf()
    )
}