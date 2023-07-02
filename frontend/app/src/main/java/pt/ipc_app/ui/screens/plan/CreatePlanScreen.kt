package pt.ipc_app.ui.screens.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.service.models.dailyList.DailyListInput
import pt.ipc_app.service.models.exercises.ExerciseInput
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.components.exercises.ExercisesInfoPagination
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun CreatePlanScreen(
    exercises: List<ExerciseInfo>,
    onPlanCreation: (PlanInput) -> Unit = { },
    onExercisesPaginationClick: (Int) -> Unit = { }
) {
    var planTitle by remember { mutableStateOf("") }

    var plan: PlanInput by remember { mutableStateOf(PlanInput(planTitle, listOf(DailyListInput()))) }

    var daySelected: Int by remember { mutableStateOf(0) }
    var dayCounter: Int by remember { mutableStateOf(1) }

    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        ) {

            Text(
                text = stringResource(id = R.string.create_plan_screen_title),
                style = MaterialTheme.typography.h5,
                color = Color.Black,
            )

            CustomTextField(
                fieldType = TextFieldType.PLAN_NAME,
                textToDisplay = planTitle,
                updateText = { planTitle = it },
                isToTrim = false,
                iconImageVector = Icons.Default.Edit,
            )

            DaysOfWeekRowWithoutLocalDate(
                daySelected = daySelected,
                totalDays = dayCounter,
                onDaySelected = { daySelected = it },
                onDayAdded = {
                    plan = plan.addDailyList(DailyListInput())
                    dayCounter++
                }
            )
            ExercisesInfoPagination(
                exercises = exercises,
                onExerciseChosen = { plan = plan.addExerciseInDailyList(daySelected, ExerciseInput(it.exeID, it.exeSets, it.exeReps)) },
                isClickExerciseEnabled = { !plan.dailyLists[daySelected]!!.containsExercise(it.id) },
                onPaginationClick = onExercisesPaginationClick,
                modifier = Modifier.height(320.dp)
            )

            Button(
                onClick = { onPlanCreation(plan.copy(planTitle)) },
                enabled = planTitle.isNotEmpty(),
                shape = CircleShape,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Plan")
            }
        }
    }
}

@Preview
@Composable
fun CreatePlanScreenPreview() {
    CreatePlanScreen(
        exercises = listOf()
    )
}