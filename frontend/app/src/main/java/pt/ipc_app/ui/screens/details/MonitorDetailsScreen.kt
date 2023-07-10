package pt.ipc_app.ui.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.models.users.Rating
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.TextFieldType
import java.util.*

@Composable
fun MonitorDetailsScreen(
    monitor: MonitorOutput,
    profilePicture: @Composable () -> Unit = { },
    onSendEmailRequest: () -> Unit = { },
    onRequestedConnection: (String) -> Unit = { },
    onRatedMonitor: (Int) -> Unit = { }
) {
    var comment by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
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

        if (!monitor.isMyMonitor && !monitor.requested) {
            Spacer(modifier = Modifier.padding(top = 50.dp))

            CustomTextField(
                fieldType = TextFieldType.REQUEST_CONNECTION,
                textToDisplay = comment,
                updateText = { comment = it },
                isToTrim = false,
                iconImageVector = Icons.Default.Comment
            )
            Button(
                onClick = { onRequestedConnection(comment) },
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