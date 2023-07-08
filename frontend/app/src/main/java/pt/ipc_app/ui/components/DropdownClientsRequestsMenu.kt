package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.service.models.requests.RequestInformation

@Composable
fun DropdownClientsRequestsMenu(
    requestsOfMonitor: List<RequestInformation>,
    onClientRequestDecided: (RequestInformation, decision: Boolean) -> Unit = { _,_ -> },
    onDismissRequest: () -> Unit = { }
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismissRequest
    ) {
        requestsOfMonitor.forEach { client ->
            DropdownMenuItem(onClick = { }) {
                Text(
                    text = "${client.clientName} sent you a request",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Spacer(modifier = Modifier.weight(0.1f))
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Accept Client",
                    tint = Color.Green,
                    modifier = Modifier.clickable {
                        onClientRequestDecided(
                            requestsOfMonitor.first { it.clientID == client.clientID }, true
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Decline Client",
                    tint = Color.Red,
                    modifier = Modifier.clickable {
                        onClientRequestDecided(
                            requestsOfMonitor.first { it.clientID == client.clientID }, false
                        )
                    }
                )
            }
        }
    }
}