package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.ExerciseInfo

@Composable
fun ExerciseInfoRow(
    exercise: ExerciseInfo,
    alreadyInDailyList: Boolean,
    onExerciseAdd: (ExerciseInfo) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable {
                onExerciseAdd(exercise)
            }
            .padding(8.dp)
    ) {
        Column {
            Text(exercise.title)
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row {
            AddExerciseIcon(alreadyInDailyList)
        }
    }
}

@Composable
fun AddExerciseIcon(alreadyInDailyList: Boolean) {
    Icon(
        imageVector = if (alreadyInDailyList) Icons.Default.Check else Icons.Default.Add,
        contentDescription = "Exercise Info",
        tint = Color(131, 204, 46, 255)
    )
}