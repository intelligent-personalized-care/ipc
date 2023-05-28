package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.Exercise
import pt.ipc_app.domain.Plan
import pt.ipc_app.domain.user.*
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.service.models.register.PlanOutput
import pt.ipc_app.ui.components.BottomBar
import pt.ipc_app.ui.components.MonitorScreen
import pt.ipc_app.ui.components.PlanScreen
import pt.ipc_app.ui.components.plan
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientHomeScreen(
    client: UserInfo,
    monitor: Monitor? = null,
    plan: PlanOutput? = null,
    onExerciseSelect: (Exercise) -> Unit = { },
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    var notifications by remember { mutableStateOf(true) }

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
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(30.dp)
        ) {
            BadgedBox(modifier = Modifier.padding(end = 10.dp),
                badge = {
                    if (notifications)
                        Badge(
                            Modifier
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.BottomEnd)
                        )
                }
            ) {
                Icon(
                    imageVector = if (notifications) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                    contentDescription = "notification icon",
                    tint = Color.Black,
                    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { if (notifications) notifications = false }
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 130.dp)
        ) {

            monitor?.let {
                MonitorScreen(
                    monitor = it
                )
            }
            
            Spacer(modifier = Modifier.padding(top = 80.dp))

            plan?.let {
                PlanScreen(
                    plan = it,
                    onExerciseSelect = { ex -> onExerciseSelect(ex) }
                )
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
fun ClientHomeScreenPreview() {
    ClientHomeScreen(
        client = UserInfo("", "Test", "", Role.CLIENT),
        monitor = Monitor("Miguel", "miguel@gmail.com", "Aa123456@", null, "Physiotherapist"),
        plan = plan
    )
}