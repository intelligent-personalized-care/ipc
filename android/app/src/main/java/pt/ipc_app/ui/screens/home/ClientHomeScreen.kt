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
import pt.ipc_app.domain.Exercise
import pt.ipc_app.domain.user.*
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.models.PlanOutput
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.screens.AppScreen
import java.time.LocalDate
import java.util.*

@Composable
fun ClientHomeScreen(
    client: UserInfo,
    monitor: MonitorOutput? = null,
    plan: PlanOutput? = null,
    onMonitorClick: () -> Unit = { },
    onExerciseSelect: (Exercise) -> Unit = { },
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    var notifications by remember { mutableStateOf(true) }

    var daySelected: LocalDate by remember { mutableStateOf(LocalDate.now()) }

    AppScreen {
        Row(
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hello) + " ${client.name}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.End
            )
        }

        NotificationIcon(
            notifications = notifications,
            onClick = { if (notifications) notifications = false }
        )

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
                    text = if (plan != null) "${plan.plan.title} - ${plan.plan.duration} days"
                            else "No current plan assigned"
                )


                DaysOfWeekRow(
                    daySelected = daySelected,
                    onDaySelected = { daySelected = it }
                )

                plan?.let {
                    PlanScreen(
                        plan = it,
                        daySelected = daySelected,
                        onExerciseSelect = { ex -> onExerciseSelect(ex) }
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                onHomeClick = onHomeClick,
                onExercisesClick = onExercisesClick,
                onUserInfoClick = onUserInfoClick,
                onAboutClick = onAboutClick
            )
        }
    }
}

@Preview
@Composable
fun ClientHomeScreenWithoutMonitorAndPlanPreview() {
    ClientHomeScreen(
        client = UserInfo("", "Test", "", Role.CLIENT)
    )
}

@Preview
@Composable
fun ClientHomeScreenWithoutPlanPreview() {
    ClientHomeScreen(
        client = UserInfo("", "Test", "", Role.CLIENT),
        monitor = MonitorOutput(UUID.randomUUID(), "Miguel", "miguel@gmail.com", 4.8F)
    )
}

@Preview
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen(
        client = UserInfo("", "Test", "", Role.CLIENT),
        monitor = MonitorOutput(UUID.randomUUID(), "Miguel", "miguel@gmail.com", 4.8F),
        plan = plan
    )
}