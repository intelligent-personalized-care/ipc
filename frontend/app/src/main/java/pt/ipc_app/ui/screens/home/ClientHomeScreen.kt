package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.DailyList
import pt.ipc_app.domain.Plan
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.domain.user.*
import pt.ipc_app.session.UserInfo
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.models.users.Rating
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.exercises.DailyExercisesList
import pt.ipc_app.ui.components.exercises.planTest
import java.time.LocalDate
import java.util.*

@Composable
fun ClientHomeScreen(
    client: UserInfo,
    monitor: MonitorOutput? = null,
    plan: Plan? = null,
    onMonitorClick: () -> Unit = { },
    onExerciseSelect: (ExerciseTotalInfo) -> Unit = { }
) {
    var notifications by remember { mutableStateOf(true) }

    var daySelected: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    var dailyListSelected: DailyList? by remember { mutableStateOf(null) }

    dailyListSelected = plan?.getListOfDayIfExists(daySelected)

    Row(
        modifier = Modifier.padding(30.dp)
    ) {
        Text(
            text = stringResource(id = R.string.hello) + " ${client.name}",
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.End
        )
    }
    Row {
        Spacer(modifier = Modifier.weight(0.1f))
        NotificationIcon(
            notifications = notifications,
            onClick = { if (notifications) notifications = false }
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 130.dp)
    ) {
        MonitorRow(
            monitor = monitor,
            onMonitorClick = onMonitorClick
        )

        Spacer(modifier = Modifier.padding(top = 80.dp))

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (plan != null) "${plan.title} - ${plan.dailyLists.size} days"
                        else "No current plan assigned"
            )

            DaysWithLocalDateRow(
                days = daysOfWeek(LocalDate.now()),
                daySelected = daySelected,
                onDaySelected = {
                    daySelected = it
                    dailyListSelected = plan?.getListOfDayIfExists(it)
                }
            )

            DailyExercisesList(
                dailyListSelected = dailyListSelected,
                onExerciseSelect = { ex ->
                    onExerciseSelect(
                        ExerciseTotalInfo(
                            planId = plan!!.id,
                            dailyListId = dailyListSelected!!.id,
                            exercise = ex
                        )
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ClientHomeScreenWithoutMonitorAndPlanPreview() {
    ClientHomeScreen(
        client = UserInfo(UUID.randomUUID().toString(), "Test", "", "", Role.CLIENT)
    )
}

@Preview
@Composable
fun ClientHomeScreenWithoutPlanPreview() {
    ClientHomeScreen(
        client = UserInfo(UUID.randomUUID().toString(), "Test", "", "", Role.CLIENT),
        monitor = MonitorOutput(UUID.randomUUID(), "Miguel", "miguel@gmail.com", Rating(4.8F, 3))
    )
}

@Preview
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen(
        client = UserInfo(UUID.randomUUID().toString(), "Test", "", "", Role.CLIENT),
        monitor = MonitorOutput(UUID.randomUUID(), "Miguel", "miguel@gmail.com", Rating(4.8F, 3)),
        plan = planTest
    )
}