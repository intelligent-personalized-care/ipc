package pt.ipc_app.ui.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.R
import pt.ipc_app.service.models.plans.PlanInfoOutput
import pt.ipc_app.service.models.users.ClientOfMonitor
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun ClientDetailsScreen(
    client: ClientOfMonitor,
    profilePictureUrl: String,
    isMyClient: Boolean = true,
    onSendEmailRequest: () -> Unit = { },
    plans: List<PlanInfoOutput> = listOf(),
    onAssociatePlan: (Int, String) -> Unit = { _, _ -> }
) {
    var showPlansOfClient by remember { mutableStateOf(true) }
    var showPlansOfMonitor by remember { mutableStateOf(false) }

    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(R.string.client_details_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            ProfilePicture(url = profilePictureUrl)
            Text(
                text = client.name,
                style = MaterialTheme.typography.h6
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
                Text(
                    text = client.email,
                    modifier = Modifier.clickable {
                        onSendEmailRequest()
                    }
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.birthDate) + ": ${client.birthDate}"
                )
                Text(
                    text = stringResource(id = R.string.weight) + ": ${client.weight}"
                )
                Text(
                    text = stringResource(id = R.string.height) + ": ${client.height}"
                )
                Text(
                    text = stringResource(id = R.string.physicalCondition) + ": ${client.physicalCondition}"
                )
            }


            if (isMyClient) {
                Row {
                    Button(
                        onClick = {
                            showPlansOfClient = !showPlansOfClient
                            showPlansOfMonitor = false
                        },
                        colors = if (showPlansOfClient) ButtonDefaults.buttonColors(backgroundColor = Color(14, 145, 14, 255))
                            else ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        modifier = Modifier
                            .height(40.dp)
                            .width(150.dp)
                    ) {
                        Text(
                            text = "Client Plans",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = {
                            showPlansOfMonitor = !showPlansOfMonitor
                            showPlansOfClient = false
                        },
                        colors = if (showPlansOfMonitor) ButtonDefaults.buttonColors(backgroundColor = Color(14, 145, 14, 255))
                        else ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                        modifier = Modifier
                            .height(40.dp)
                            .width(150.dp)
                    ) {
                        Text(
                            text = "Associate Plan",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                if (showPlansOfClient) {
                    ClientPlansList(
                        plans = client.plans
                    )
                }

                if (showPlansOfMonitor) {
                    var startDate by remember { mutableStateOf("") }

                    val dt = DatePicker(
                        onDateSelected = { startDate = it }
                    )
                    MyDatePicker(
                        fieldType = TextFieldType.PLAN_START_DATE,
                        value = startDate,
                        onValueChange = { startDate = it },
                        onClick = { dt.show() }
                    )

                    if (startDate.isNotEmpty()) {
                        MonitorPlansList(
                            plans = plans,
                            onAssociatePlan = { onAssociatePlan(it, startDate) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ClientDetailsScreenPreview() {
    ClientDetailsScreen(client = ClientOfMonitor(UUID.randomUUID(), "Mike", ""), "")
}