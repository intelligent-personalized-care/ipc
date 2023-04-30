package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.domain.user.Role
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo
) {
    var notifications by remember { mutableStateOf(false) }

    AppScreen {
        Row {
            Text("Hello ${monitor.name}")
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

        }
    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo("Test", "", Role.MONITOR)
    )
}