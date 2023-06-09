package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.DailyExercise

@Composable
fun DailyExerciseRow(
    exercise: DailyExercise,
    onExerciseSelect: (DailyExercise) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable {
                onExerciseSelect(exercise)
            }
            .padding(8.dp)
    ) {
        Column {
            Text(exercise.title)
            Text(
                text = "${exercise.reps} reps - ${exercise.sets} sets",
                style = MaterialTheme.typography.overline,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row {
            ExerciseIconDone(exercise.isDone)
        }
    }
}

@Composable
fun ExerciseIconDone(done: Boolean) {
    Icon(
        imageVector = if (done) Icons.Default.Check else Icons.Default.HourglassBottom,
        contentDescription = "Exercise is done",
        tint = if (done) Color(131, 204, 46, 255)
        else Color(255, 217, 102, 255)
    )
}