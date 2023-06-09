package pt.ipc_app.ui.screens.exercises.info

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.ui.components.VideoPlayer
import java.util.*

@Composable
fun ExerciseScreen(
    exercise: Exercise,
    exercisePreviewUrl: String,
    isToRecord: Boolean = true,
    onRecordClick: () -> Unit = { }
) {
    var isPlaying by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.padding(16.dp)
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

        VideoPlayer(url = exercisePreviewUrl, playing = isPlaying)

        Spacer(modifier = Modifier.padding(top = 50.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = exercise.exeDescription,
            style = MaterialTheme.typography.caption
        )

        if (isToRecord) {
            Spacer(modifier = Modifier.padding(top = 50.dp))

            Button(
                onClick = {
                    isPlaying = false
                    onRecordClick()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
                Text(
                    text = "Record Video",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ExerciseScreenPreview() {
    ExerciseScreen(
        exercise = Exercise(UUID.randomUUID(), "Push ups", "Contract your abs and tighten your core by pulling your belly button toward your spine. \n" +
                "Inhale as you slowly bend your elbows and lower yourself to the floor, until your elbows are at a 90-degree angle.\n" +
                "Exhale while contracting your chest muscles and pushing back up through your hands, returning to the start position.", 15, 3),
        exercisePreviewUrl = ""
    )
}
