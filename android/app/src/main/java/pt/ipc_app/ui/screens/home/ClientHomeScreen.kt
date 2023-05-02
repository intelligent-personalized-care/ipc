package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.*
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.ui.components.BottomBar
import pt.ipc_app.ui.components.MonitorScreen
import pt.ipc_app.ui.components.PlanScreen
import pt.ipc_app.ui.components.plan
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientHomeScreen(
    client: UserInfo,
    monitor: Monitor? = null,
    plan: Plan? = null
) {
    var notifications by remember { mutableStateOf(true) }

    AppScreen {
        Row(
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.hello)+ " ${client.name}",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.End)
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(30.dp)
        ) {
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { if (notifications) notifications = false }
                    )
            ) {
                Icon(
                    imageVector = if (notifications) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                    contentDescription = "Notification"
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 150.dp)
        ) {

            monitor?.let {
                MonitorScreen(
                    monitor = it
                )
            }
            
            Spacer(modifier = Modifier.padding(top = 70.dp))

            plan?.let {
                PlanScreen(
                    plan = it
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar()
        }
    }
}

@Preview
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen(
        client = UserInfo("Test", "", Role.CLIENT),
        monitor = Monitor("Miguel", "miguel@gmail.com", "Aa123456@", null, "Physiotherapist"),
        plan = plan
    )
}