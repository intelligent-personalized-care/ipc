package pt.ipc_app.ui.screens.exercises.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.ui.components.exercises.ExercisesInfoPagination

@Composable
fun ExercisesListScreen(
    exercises: List<ExerciseInfo>,
    onExerciseClick: (Exercise) -> Unit = { },
    onPaginationClick: (Int) -> Unit = { },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(30.dp)
    ) {
        Text(
            text = stringResource(R.string.exercises_title),
            style = MaterialTheme.typography.h4
        )
        ExercisesInfoPagination(
            exercises = exercises,
            onExerciseChosen = onExerciseClick,
            onPaginationClick = onPaginationClick,
            modifier = Modifier.height(620.dp)
        )
    }
}