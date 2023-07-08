package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseInfo

@Composable
fun ExercisesInfoPagination(
    exercises: List<ExerciseInfo>,
    onExerciseChosen: (Exercise) -> Unit = { },
    isClickExerciseEnabled: (ExerciseInfo) -> Boolean = { true },
    onPaginationClick: (Int) -> Unit = { },
    modifier: Modifier = Modifier
) {
    var curSkip by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
    ) {
        ExercisesInfoList(
            exercises = exercises,
            isClickExerciseEnabled = isClickExerciseEnabled,
            onExerciseClick = onExerciseChosen
        )
    }

    Row(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SkipPrevious,
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    enabled = curSkip != 0,
                    onClick = {
                        curSkip -= 10
                        onPaginationClick(curSkip)
                    }
                )
                .padding(end = 230.dp)
        )

        Icon(
            imageVector = Icons.Default.SkipNext,
            contentDescription = null,
            modifier = Modifier.clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                enabled = exercises.size == 10,
                onClick = {
                    curSkip += 10
                    onPaginationClick(curSkip)
                }
            )
        )
    }
}