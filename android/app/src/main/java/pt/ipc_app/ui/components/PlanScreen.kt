package pt.ipc_app.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.domain.user.DailyList
import pt.ipc_app.domain.user.Exercise
import pt.ipc_app.domain.user.Plan
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun PlanScreen(
    plan: Plan,
    onDaySelect: (DailyList) -> Unit = {},
    onExerciseSelect: (Exercise) -> Unit = {}
) {

    var dailyListSelected: DailyList? by remember { mutableStateOf(DailyList(
        id = 1,
        day = LocalDate.now(),
        exercises = listOf(
            Exercise(1, "Push ups"),
            Exercise(2, "Abs"),
            Exercise(3, "Leg extension")
        )
    )) }
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = plan.title
        )
        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            plan.dailyLists.forEach {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color(131, 129, 129, 255), RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (it.day == LocalDate.now())
                                Color(170, 233, 98, 255)
                            else if (dailyListSelected?.id != it.id)
                                Color(231, 231, 231, 255)
                            else
                                Color(174, 237, 245, 255)
                        )
                        .clickable {
                            dailyListSelected = it
                            onDaySelect(it)
                        }
                ) {
                    Text(it.day.dayOfMonth.toString())
                    Text(
                        text = it.day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(top = 20.dp)
                .border(1.dp, Color(204, 202, 202, 255))
        ) {
            dailyListSelected?.let {
                it.exercises.forEach { ex ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp)
                            .background(Color.White)
                            .clickable {
                                onExerciseSelect(ex)
                            }
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(ex.title)
                            Text(
                                text = "${ex.reps} reps - ${ex.sets} sets",
                                style = MaterialTheme.typography.overline,
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Row {
                            ExerciseIconDone(ex)
                        }
                    }
                    if (ex.id != it.exercises.last().id)
                        Spacer(modifier = Modifier.height(5.dp))

                }
            }
        }
    }
}

@Composable
fun ExerciseIconDone(ex: Exercise) {
    val isDone = ex.isDone()
    Icon(
        imageVector = if (isDone) Icons.Default.Check else Icons.Default.HourglassBottom,
        contentDescription = "Exercise is done",
        tint = if (isDone) Color(131, 204, 46, 255)
        else Color(255, 217, 102, 255)
    )
}

@Preview
@Composable
fun PlanScreenPreview() {
    PlanScreen(
        plan = plan
    )
}

val plan = Plan(
    id = 1,
    title = "PlanTest",
    dailyLists = listOf(
        DailyList(
            id = 1,
            day = LocalDate.of(2023, 5, 1),
            exercises = listOf(
                Exercise(1, "Push ups"),
                Exercise(2, "Abs"),
                Exercise(3, "Leg extension")
            )
        ),
        DailyList(
            id = 2,
            day = LocalDate.of(2023, 5, 2),
            exercises = listOf(
                Exercise(1, "Push ups2"),
                Exercise(2, "Abs2"),
                Exercise(3, "Leg extension2")
            )
        ),
        DailyList(
            id = 3,
            day = LocalDate.of(2023, 5, 3),
            exercises = listOf(
                Exercise(1, "Push ups3"),
                Exercise(2, "Abs3"),
                Exercise(3, "Leg extension3")
            )
        ),
        DailyList(
            id = 4,
            day = LocalDate.of(2023, 5, 4),
            exercises = listOf(
                Exercise(1, "Push ups4"),
                Exercise(2, "Abs4"),
                Exercise(3, "Leg extension4")
            )
        ),
        DailyList(
            id = 5,
            day = LocalDate.of(2023, 5, 5),
            exercises = listOf(
                Exercise(1, "Push ups5"),
                Exercise(2, "Abs5"),
                Exercise(3, "Leg extension5")
            )
        ),
        DailyList(
            id = 6,
            day = LocalDate.of(2023, 5, 6),
            exercises = listOf(
                Exercise(1, "Push ups6"),
                Exercise(2, "Abs6"),
                Exercise(3, "Leg extension6")
            )
        ),
        DailyList(
            id = 7,
            day = LocalDate.of(2023, 5, 7),
            exercises = listOf(
                Exercise(1, "Push ups7"),
                Exercise(2, "Abs7"),
                Exercise(3, "Leg extension7")
            )
        )
    )
)