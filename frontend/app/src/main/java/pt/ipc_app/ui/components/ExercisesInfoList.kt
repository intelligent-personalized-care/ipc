package pt.ipc_app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.service.models.dailyList.DailyListInput

@Composable
fun ExercisesInfoList(
    exercises: List<ExerciseInfo>,
    isExerciseAlreadyInDailyList: (ExerciseInfo) -> Boolean,
    onExerciseAdd: (Exercise) -> Unit
) {

    Spacer(modifier = Modifier.padding(top = 20.dp))

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .border(1.dp, Color(204, 202, 202, 255))
    ) {
        exercises.forEach { ex ->
            ExerciseInfoRow(
                exercise = ex,
                alreadyInDailyList = isExerciseAlreadyInDailyList(ex),
                onExerciseAdd = onExerciseAdd
            )
        }
    }
}