package pt.ipc_app.ui.components.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType

@Composable
fun ExerciseInfoRow(
    exercise: ExerciseInfo,
    clickExerciseEnabled: Boolean,
    onExerciseAdd: (Exercise) -> Unit = { }
) {
    var clicked by remember { mutableStateOf(false) }

    var sets by remember { mutableStateOf(0) }
    var reps by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp)
            .background(Color.White)
            .clickable { clicked = !clicked }
            .padding(8.dp)
    ) {
        Column {
            Text(exercise.title)
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row {
            if (!clickExerciseEnabled)
                AddExerciseIcon(true)
            else if (clicked)
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = "DropUp"
                )
            else
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "DropDown"
                )
        }
    }
    if (clicked && clickExerciseEnabled) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .width(280.dp)
                .height(60.dp)
        ) {
            CustomTextField(
                fieldType = TextFieldType.EXERCISE_SETS,
                textToDisplay = sets.toString(),
                updateText = { sets = it.toInteger(2) },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .weight(0.5f)
            )
            CustomTextField(
                fieldType = TextFieldType.EXERCISE_REPS,
                textToDisplay = reps.toString(),
                updateText = { reps = it.toInteger(2) },
                keyboardType = KeyboardType.Number,
                modifier = Modifier
                    .weight(0.5f)
            )
            Box(
                modifier = Modifier
                    .clickable {
                        if (sets != 0 && reps != 0) {
                            onExerciseAdd(
                                Exercise(
                                    exeID = exercise.id,
                                    exeTitle = exercise.title,
                                    exeDescription = exercise.description,
                                    exeSets = sets,
                                    exeReps = reps
                                )
                            )
                            clicked = !clicked
                        }
                    }
                    .padding(start = 16.dp)
            ) {
                AddExerciseIcon(false)
            }
        }
    }
}

private fun String.toInteger(maxLength: Int): Int {
    return if (isEmpty() || toIntOrNull() == null || length > maxLength) 0
    else toInt()
}

@Composable
fun AddExerciseIcon(alreadyInDailyList: Boolean) {
    Icon(
        imageVector = if (alreadyInDailyList) Icons.Default.Check else Icons.Default.Add,
        contentDescription = "Exercise Info",
        tint = Color(131, 204, 46, 255)
    )
}