package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.exercise.DailyExercise
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.models.exercises.ClientDailyExercises
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestInformation
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.exercises.ClientExercisesToDoList
import java.time.LocalDate
import java.util.*

const val MonitorHomeScreenTag = "MonitorHomeScreen"

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo,
    clientsOfMonitor: List<ClientOutput>,
    requestsOfMonitor: List<RequestInformation>,
    clientsExercisesToDo: List<ClientDailyExercises>,
    clientsExercisesToDoProgressState: ProgressState = ProgressState.IDLE,
    onDaySelected: (LocalDate) -> Unit = { },
    onClientSelected: (ClientOutput) -> Unit = { },
    onClientRequestDecided: (RequestInformation, ConnectionRequestDecisionInput) -> Unit = { _, _ -> },
    onClientExercisesSelected:  (clientId: UUID, clientName: String, planDate: LocalDate) -> Unit = { _, _, _ -> }
) {
    var notificationsExpanded by remember { mutableStateOf(false) }

    var daySelected: LocalDate by remember { mutableStateOf(LocalDate.now()) }

    Row(
        modifier = Modifier
            .testTag(MonitorHomeScreenTag)
            .padding(30.dp)
    ) {
        Text(
            text = stringResource(id = R.string.hello) + " ${monitor.name}",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.End
        )
    }

    Row {
        Spacer(modifier = Modifier.weight(0.1f))
        Column {
            NotificationIcon(
                notifications = requestsOfMonitor.isNotEmpty(),
                onClick = { if (requestsOfMonitor.isNotEmpty()) notificationsExpanded = true }
            )

            if (notificationsExpanded)
                DropdownClientsRequestsMenu(
                    requestsOfMonitor = requestsOfMonitor,
                    onDismissRequest = { notificationsExpanded = false },
                    onClientRequestDecided = { request, decision ->
                        onClientRequestDecided(request, ConnectionRequestDecisionInput(decision))
                        notificationsExpanded = false
                    }
                )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 130.dp)
    ) {
        ClientsTable(
            columnText = stringResource(id = R.string.my_clients),
            clients = clientsOfMonitor,
            onClientClick = { onClientSelected(it) }
        )

        DaysWithLocalDateRow(
            days = daysOfWeek(LocalDate.now()),
            daySelected = daySelected,
            onDaySelected = {
                daySelected = it
                onDaySelected(daySelected)
            }
        )

        if (clientsExercisesToDoProgressState == ProgressState.WAITING)
            CircularProgressIndicator()
        else
            ClientExercisesToDoList(
                exercisesOfClients = clientsExercisesToDo,
                onClientSelect = { onClientExercisesSelected(it.id, it.name, daySelected) }
            )

    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo(UUID.randomUUID().toString(), "Test", "", "", Role.MONITOR),
        clientsOfMonitor = listOf(ClientOutput(UUID.randomUUID(), "Tiago", "")),
        requestsOfMonitor = listOf(),
        clientsExercisesToDo = listOf(
            ClientDailyExercises(
                id = UUID.randomUUID(),
                name = "Tiago",
                planId = 1,
                dailyListId = 1,
                exercises = listOf(
                    DailyExercise(1, UUID.randomUUID(), "Push ups", "", "")
                )
            )
        )
    )
}