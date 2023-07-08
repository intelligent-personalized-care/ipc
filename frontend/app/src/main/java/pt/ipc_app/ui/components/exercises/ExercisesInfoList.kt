package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.ui.components.exercises.ExerciseInfoRow

@Composable
fun ExercisesInfoList(
    exercises: List<ExerciseInfo>,
    isClickExerciseEnabled: (ExerciseInfo) -> Boolean,
    onExerciseClick: (Exercise) -> Unit
) {

    Spacer(modifier = Modifier.padding(top = 20.dp))

    LazyColumn(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .border(1.dp, Color(204, 202, 202, 255))
    ) {
        items(exercises) { ex ->
            ExerciseInfoRow(
                exercise = ex,
                clickExerciseEnabled = isClickExerciseEnabled(ex),
                onExerciseAdd = onExerciseClick
            )
        }
    }
}