package pt.ipc_app.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.service.models.plans.PlanInfoOutput
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.DatePicker
import pt.ipc_app.ui.components.MonitorPlansList
import pt.ipc_app.ui.components.MyDatePicker
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun ClientDetailsScreen(
    client: ClientOutput,
    isMyClient: Boolean = true,
    onSendEmailRequest: () -> Unit = { },
    plans: List<PlanInfoOutput> = listOf(),
    onAssociatePlan: (Int, String) -> Unit = { _, _ -> }
) {
    var showPlans by remember { mutableStateOf(false) }

    AppScreen {
        Column {
            Text(client.name)
            Button(onClick = onSendEmailRequest) {
                Text(client.email)
            }

            if (isMyClient) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = { showPlans = !showPlans },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(14, 145, 14, 255)),
                    ) {
                        Text("Associate Plan")
                    }

                    if (showPlans) {
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

}

@Preview
@Composable
fun ClientDetailsScreenPreview() {
    ClientDetailsScreen(client = ClientOutput(UUID.randomUUID(), "Mike", ""))
}