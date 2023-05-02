package pt.ipc_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.preferences.UserInfo
import pt.ipc_app.ui.components.BottomBar
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo,
    onClientsRequest: () -> Unit
) {
    var notifications by remember { mutableStateOf(false) }

    AppScreen {
        Row {
            Text(stringResource(id = R.string.hello) + " ${monitor.name}")
        }
        Row(
            horizontalArrangement = Arrangement.End
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
                    imageVector = if (notifications) Icons.Filled.Notifications else Icons.Outlined.Notifications,
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
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar()
        }

    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo("Test", "", Role.MONITOR),
        onClientsRequest = {}
    )
}