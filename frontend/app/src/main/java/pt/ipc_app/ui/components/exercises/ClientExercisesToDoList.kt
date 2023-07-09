package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.exercises.ClientDailyExercises
import java.time.LocalDate
import java.util.UUID

@Composable
fun ClientExercisesToDoList(
    exercisesOfClients: List<ClientDailyExercises>,
    onClientSelect: (ClientDailyExercises) -> Unit = { }
) {

    if (exercisesOfClients.isNotEmpty()) {
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .border(1.dp, Color(204, 202, 202, 255))
        ) {
            items(exercisesOfClients) { ex ->
                ClientExercisesToDoRow(
                    clientExercise = ex,
                    onClientSelect = onClientSelect
                )
            }
        }
    } else {
        Text("No one has exercises for this day.", textAlign = TextAlign.Center)
    }

}
