package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.domain.user.Client
import pt.ipc_app.domain.user.Monitor
import pt.ipc_app.domain.user.Plan
import pt.ipc_app.domain.user.Role
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.ui.components.MonitorScreen
import pt.ipc_app.ui.components.PlanScreen
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientHomeScreen(
    client: UserInfo,
    monitor: Monitor? = null,
    plan: Plan? = null
) {
    var notifications by remember { mutableStateOf(false) }

    AppScreen {
        Row {
            Text("Hello ${client.name}")
        }
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier.clickable(
                    onClick = { notifications = !notifications }
                )
            ) {
                Icon(
                    imageVector = if (notifications) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                    contentDescription = "Notification"
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            monitor?.let {
                MonitorScreen(
                    monitor = it
                )
            }

            plan?.let {
                PlanScreen(
                    plan = it
                )
            }
        }
    }
}

@Preview
@Composable
fun ClientHomeScreenPreview() {
    ClientHomeScreen(
        client = UserInfo("Test", "", Role.CLIENT)
    )
}