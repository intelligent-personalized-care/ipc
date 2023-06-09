package pt.ipc_app.ui.components.exercises


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.DailyList
import pt.ipc_app.domain.exercise.DailyExercise
import pt.ipc_app.domain.Plan
import java.time.LocalDate
import java.util.*

@Composable
fun DailyExercisesList(
    dailyListSelected: DailyList? = null,
    onExerciseSelect: (DailyExercise) -> Unit = { }
) {

    if (dailyListSelected != null) {
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .border(1.dp, Color(204, 202, 202, 255))
        ) {
                items(dailyListSelected.exercises) { ex ->
                    DailyExerciseRow(
                        exercise = ex,
                        onExerciseSelect = onExerciseSelect
                    )
                }
        }
    } else {
        Text("No exercises for this day.", textAlign = TextAlign.Center)
        Row {
            Text(text = "Day off")
            Icon(
                imageVector = Icons.Default.EmojiEmotions,
                contentDescription = "Emoji",
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }

}

@Preview
@Composable
fun PlanScreenPreview() {
    DailyExercisesList(
        dailyListSelected = planTest.dailyLists.first()
    )
}

val planTest = Plan(
    1,
    title = "PlanTest",
    startDate = LocalDate.of(2023, 7, 6).toString(),
    dailyLists = listOf(
        DailyList(0,
            exercises = listOf(
                DailyExercise(1, UUID.randomUUID(), "Push ups", "", ""),
                DailyExercise(2, UUID.randomUUID(), "Abs", "", ""),
                DailyExercise(3, UUID.randomUUID(), "Shoulder press", "", "",3,5)
            )
        ),
        DailyList(1,
            exercises = listOf(
                DailyExercise(1, UUID.randomUUID(), "Push ups2", "", ""),
                DailyExercise(2, UUID.randomUUID(), "Abs2", "", ""),
                DailyExercise(3, UUID.randomUUID(), "Leg extension2", "", "")
            )
        ),
        DailyList(2,
            exercises = listOf(
                DailyExercise(1, UUID.randomUUID(), "Push ups3", "", ""),
                DailyExercise(2, UUID.randomUUID(), "Abs3", "", ""),
                DailyExercise(3, UUID.randomUUID(), "Squats", "", "")
            )
        ),
        DailyList(3,
            exercises = listOf(
                DailyExercise(1, UUID.randomUUID(), "Push ups4", "", ""),
                DailyExercise(2, UUID.randomUUID(), "Abs4", "", ""),
                DailyExercise(3, UUID.randomUUID(), "Leg extension4", "", "")
            )
        ),
        DailyList(4,
            exercises = listOf(
                DailyExercise(1, UUID.randomUUID(), "Push ups5", "", ""),
                DailyExercise(2, UUID.randomUUID(), "Abs5", "", ""),
                DailyExercise(3, UUID.randomUUID(), "Leg extension5", "", "")
            )
        )
    )
)