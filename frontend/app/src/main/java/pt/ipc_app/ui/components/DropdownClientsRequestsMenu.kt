package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.service.models.requests.RequestInformation
import java.util.*

@Composable
fun DropdownClientsRequestsMenu(
    requestsOfMonitor: List<RequestInformation>,
    onClientRequestDecided: (RequestInformation, decision: Boolean) -> Unit = { _,_ -> },
    onDismissRequest: () -> Unit = { }
) {
    var clientRequestIdToDisplay: UUID? by remember { mutableStateOf(null) }

    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismissRequest
    ) {
        requestsOfMonitor.forEach { client ->
            DropdownMenuItem(onClick = { }) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${client.clientName} sent you a request",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = { clientRequestIdToDisplay = client.clientID }
                                )
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
                                    requestsOfMonitor.first { it.clientID == client.clientID },
                                    false
                                )
                            }
                        )
                    }
                    if (clientRequestIdToDisplay != null && clientRequestIdToDisplay == client.clientID && client.requestText != null)
                        Text(
                            text = client.requestText,
                            style = MaterialTheme.typography.overline
                        )
                }
            }
        }
    }
}