package pt.ipc_app.ui.screens.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.DailyList
import pt.ipc_app.domain.Plan
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.domain.toLocalDate
import pt.ipc_app.ui.components.DaysWithLocalDateRow
import pt.ipc_app.ui.components.exercises.DailyExercisesList
import pt.ipc_app.ui.components.exercises.planTest
import java.time.LocalDate

@Composable
fun PlanScreen(
    plan: Plan?,
    clientName: String,
    onExerciseSelect: (ExerciseTotalInfo) -> Unit = { }
) {
    var daySelected: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    var dailyListSelected: DailyList? by remember { mutableStateOf(null) }

    dailyListSelected = plan?.getListOfDayIfExists(daySelected)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 30.dp)
    ) {
        Text(
            text = stringResource(R.string.plan_screen_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        plan?.let {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.h5,
            )
            Text(text = "of")
            Text(
                text = clientName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Text(text = "${plan.startDate} - ${plan.startDate.toLocalDate().plusDays(plan.dailyLists.size.toLong() - 1)}")

            DaysWithLocalDateRow(
                days = plan.days(),
                daySelected = daySelected,
                onDaySelected = {
                    daySelected = it
                    dailyListSelected = plan.getListOfDayIfExists(it)
                },
                modifier = Modifier.padding(end = 8.dp)
            )

            DailyExercisesList(
                dailyListSelected = dailyListSelected,
                onExerciseSelect = { ex ->
                    onExerciseSelect(
                        ExerciseTotalInfo(
                            planId = plan.id,
                            dailyListId = dailyListSelected!!.id,
                            exercise = ex
                        )
                    )
                }
            )
        }

    }

}

@Preview
@Composable
fun PlanScreenPreview() {
    PlanScreen(plan = planTest, clientName = "Test")
}