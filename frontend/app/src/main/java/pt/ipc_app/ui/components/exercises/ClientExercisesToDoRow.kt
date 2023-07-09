package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.exercises.ClientDailyExercises
import java.time.LocalDate
import java.util.UUID

@Composable
fun ClientExercisesToDoRow(
    clientExercise: ClientDailyExercises,
    onClientSelect: (ClientDailyExercises) -> Unit = {  }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable {
                onClientSelect(clientExercise)
            }
            .padding(8.dp)
    ) {
        Column {
            Text(clientExercise.name)
            Text(
                text = "${clientExercise.exercises.size} exercises to do",
                style = MaterialTheme.typography.overline,
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row {
            ExerciseIconDone(clientExercise.allExercisesDone())
        }
    }
}