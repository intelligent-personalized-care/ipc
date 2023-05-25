package pt.ipc_app.ui.screens.exercise

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.domain.Exercise
import pt.ipc_app.ui.components.VideoPlayer
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ExerciseScreen(
    exercise: Exercise,
    onRecordClick: () -> Unit = {}
) {
    AppScreen {
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
                    text = exercise.title.uppercase(),
                    style = MaterialTheme.typography.h6
                )
            }

            VideoPlayer(url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")

            Spacer(modifier = Modifier.padding(top = 100.dp))


            Text(
                text = "Description",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.caption
            )

            Spacer(modifier = Modifier.padding(top = 200.dp))

            Button(
                onClick = onRecordClick,
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
        exercise = Exercise(1, "Push ups", "Contract your abs and tighten your core by pulling your belly button toward your spine. \n" +
                "Inhale as you slowly bend your elbows and lower yourself to the floor, until your elbows are at a 90-degree angle.\n" +
                "Exhale while contracting your chest muscles and pushing back up through your hands, returning to the start position.", "", 15, 3)
    )
}
