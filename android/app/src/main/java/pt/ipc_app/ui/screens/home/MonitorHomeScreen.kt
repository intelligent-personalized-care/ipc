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
import pt.ipc_app.ui.components.BottomBar
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun MonitorHomeScreen(
    monitor: UserInfo,
    onClientsRequest: () -> Unit
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

        }

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                onExercisesClick = onClientsRequest
            )
        }

    }
}

@Preview
@Composable
fun MonitorHomeScreenPreview() {
    MonitorHomeScreen(
        monitor = UserInfo("", "Test", "", Role.MONITOR),
        onClientsRequest = {}
    )
}