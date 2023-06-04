package pt.ipc_app.ui.screens.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.toLocalDate
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.ui.components.DatePicker
import pt.ipc_app.ui.components.MyDatePicker
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun AddUserToPlanScreen(
    plan: PlanInput,
    onPlanCreation: (PlanInput) -> Unit = { }
) {
    var startDate by remember { mutableStateOf("") }

    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {


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
                val plan = PlanInput(plan.title, plan.dailyLists)


            }
        }
    }
}

@Preview
@Composable
fun AddUserToPlanScreen() {

}