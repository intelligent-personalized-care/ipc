package pt.ipc_app.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import pt.ipc_app.service.models.users.Rating
import pt.ipc_app.ui.components.MonitorRating
import pt.ipc_app.ui.components.RateMonitor
import pt.ipc_app.ui.components.TextEmail
import java.util.*

@Composable
fun MonitorDetailsScreen(
    monitor: MonitorOutput,
    profilePicture: @Composable () -> Unit = { },
    requestEnable: Boolean = true,
    onSendEmailRequest: () -> Unit = { },
    onRequestedConnection: () -> Unit = { },
    onRatedMonitor: (Int) -> Unit = { }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(30.dp)
    ) {
        Text(
            text = stringResource(R.string.monitor_details_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        profilePicture()
        Text(
            text = monitor.name,
            style = MaterialTheme.typography.h6
        )
        TextEmail(
            email = monitor.email,
            onClick = onSendEmailRequest,
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )
        MonitorRating(rating = monitor.rating)

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

        if (monitor.isMyMonitor)
            RateMonitor(
                modifier = Modifier.padding(top = 150.dp),
                onSubmitRating = onRatedMonitor
            )
    }
}

@Preview
@Composable
fun MonitorDetailsScreenPreview() {
    MonitorDetailsScreen(
        monitor = MonitorOutput(UUID.randomUUID(), "Mike", "", Rating(4.8F, 3))
    )
}