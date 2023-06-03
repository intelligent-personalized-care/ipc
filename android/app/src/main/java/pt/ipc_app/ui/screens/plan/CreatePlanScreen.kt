package pt.ipc_app.ui.screens.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.ui.components.*
import pt.ipc_app.ui.screens.AppScreen
import java.time.LocalDate

@Composable
fun CreatePlanScreen(
    exercises: List<ExerciseInfo>,
    onPlanCreation: (PlanInput) -> Unit = { }
) {
    var planTitle by remember { mutableStateOf("") }

    var daySelected: Int by remember { mutableStateOf(0) }


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

            if (planTitle.isNotEmpty()) {
                val plan = PlanInput(planTitle, LocalDate.now(), listOf())

                DaysOfWeekRowWithoutLocalDate(
                    daySelected = daySelected,
                    onDaySelected = {
                        daySelected = it
                    }
                )

                ExercisesInfoList(
                    exercises = exercises,
                    isExerciseAlreadyInDailyList = { true },
                )


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