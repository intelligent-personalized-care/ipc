package pt.ipc_app.ui.screens.exercise

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import pt.ipc_app.domain.Exercise

@Composable
fun ExerciseScreen(
    exercise: Exercise,
    onRecordClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(exercise.title)
        Button(
            onClick = onRecordClick
        ) {
            Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
            Text("Record Video")
        }
    }
}