package pt.ipc_app.ui.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.components.ProfilePicture
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun MonitorDetailsScreen(
    monitor: MonitorOutput,
    profilePictureUrl: String,
    requestEnable: Boolean = true,
    onSendEmailRequest: () -> Unit = { },
    onRequestedConnection: () -> Unit = { }
) {
    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(R.string.monitor_details_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            ProfilePicture(url = profilePictureUrl)
            Text(
                text = monitor.name,
                style = MaterialTheme.typography.h6
            )

        }
        Column(
            modifier = Modifier.padding(start = 30.dp, top = 300.dp)
        ) {
            Text(
                text = monitor.email,
                modifier = Modifier.clickable { onSendEmailRequest() }
            )

            if (requestEnable) {
                Button(
                    onClick = { onRequestedConnection() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(14, 145, 14, 255)),
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Request",
                        tint = Color.White
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun MonitorDetailsScreenPreview() {
    MonitorDetailsScreen(
        monitor = MonitorOutput(UUID.randomUUID(), "Mike", "", 4.8F),
        profilePictureUrl = ""
    )
}