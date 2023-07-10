package pt.ipc_app.ui.screens.exercises.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.components.VideoPlayer
import java.util.*

@Composable
fun ClientExerciseScreen(
    exercise: Exercise,
    clientExerciseUrl: String,
    setSelected: Int,
    onSetSelected: (Int) -> Unit = { },
    onFeedbackSent: (String) -> Unit = { }
) {
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = exercise.exeTitle.uppercase(),
                style = MaterialTheme.typography.h6
            )
        }

        VideoPlayer(url = clientExerciseUrl)

        LazyRow(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(exercise.exeSets) {
                Button(
                    onClick = { onSetSelected(it + 1) },
                    enabled = setSelected != it + 1,
                    modifier = Modifier.padding(2.dp).size(40.dp),
                    colors = if (setSelected != it + 1) ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)
                    else ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                ) {
                    Text(
                        text = (it + 1).toString(),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 100.dp))

        CustomTextField(
            fieldType = TextFieldType.EXERCISE_FEEDBACK,
            textToDisplay = feedback,
            updateText = { feedback = it },
            isToTrim = false,
            iconImageVector = Icons.Default.Feedback
        )

        Button(
            onClick = {
                onFeedbackSent(feedback)
                feedback = ""
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Feedback"
            )
        }

    }
}

@Preview
@Composable
fun ExerciseFeedbackScreenPreview() {
    ClientExerciseScreen(
        exercise = Exercise(UUID.randomUUID(), "Push ups", "Contract your abs and tighten your core by pulling your belly button toward your spine. \n" +
                "Inhale as you slowly bend your elbows and lower yourself to the floor, until your elbows are at a 90-degree angle.\n" +
                "Exhale while contracting your chest muscles and pushing back up through your hands, returning to the start position.", 15, 3),
        clientExerciseUrl = "",
        setSelected = 1
    )
}
