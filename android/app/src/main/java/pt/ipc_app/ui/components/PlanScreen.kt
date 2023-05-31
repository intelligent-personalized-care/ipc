package pt.ipc_app.ui.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.DailyList
import pt.ipc_app.domain.Exercise
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.models.PlanOutput
import java.time.LocalDate

@Composable
fun PlanScreen(
    plan: PlanOutput,
    daySelected: LocalDate,
    onExerciseSelect: (Exercise) -> Unit = {}
) {

    var dailyListSelected: DailyList? by remember { mutableStateOf(plan.plan.getListOfDayIfExists(daySelected)) }

    Spacer(modifier = Modifier.padding(top = 20.dp))

    if (dailyListSelected != null) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .border(1.dp, Color(204, 202, 202, 255))
        ) {
                dailyListSelected!!.exercises.forEach { ex ->
                    ExerciseRow(
                        exercise = ex,
                        onExerciseSelect = onExerciseSelect
                    )
                }
        }
    } else {
        Text("No exercises for this day.\nDay off :)", textAlign = TextAlign.Center)
    }

}

@Preview
@Composable
fun PlanScreenPreview() {
    PlanScreen(
        plan = plan,
        daySelected = LocalDate.now()
    )
}

val plan = PlanOutput(
    1,
    Plan(
        title = "PlanTest",
        startDate = LocalDate.of(2023, 6, 28),
        dailyLists = listOf(
            DailyList(
                exercises = listOf(
                    Exercise(1, "Push ups", "", ""),
                    Exercise(2, "Abs", "", ""),
                    Exercise(3, "Leg extension", "", "")
                )
            ),
            DailyList(
                exercises = listOf(
                    Exercise(1, "Push ups2", "", ""),
                    Exercise(2, "Abs2", "", ""),
                    Exercise(3, "Leg extension2", "", "")
                )
            ),
            DailyList(
                exercises = listOf(
                    Exercise(1, "Push ups3", "", ""),
                    Exercise(2, "Abs3", "", ""),
                    Exercise(3, "Squats", "1. Stand with feet a little wider than hip width, toes facing front.\n" +
                            "2. Drive your hips back—bending at the knees and ankles and pressing your knees slightly open—as you…\n" +
                            "3. Sit into a squat position while still keeping your heels and toes on the ground, chest up and shoulders back.\n" +
                            "4. Strive to eventually reach parallel, meaning knees are bent to a 90-degree angle.\n" +
                            "5. Press into your heels and straighten legs to return to a standing upright position.", "", 15, 3)
                )
            ),
            DailyList(
                exercises = listOf(
                    Exercise(1, "Push ups4", "Contract your abs and tighten your core by pulling your belly button toward your spine. \n" +
                            "Inhale as you slowly bend your elbows and lower yourself to the floor, until your elbows are at a 90-degree angle.\n" +
                            "Exhale while contracting your chest muscles and pushing back up through your hands, returning to the start position.", ""),
                    Exercise(2, "Abs4", "", ""),
                    Exercise(3, "Leg extension4", "", "")
                )
            ),
            DailyList(
                exercises = listOf(
                    Exercise(1, "Push ups5", "", ""),
                    Exercise(2, "Abs5", "", ""),
                    Exercise(3, "Leg extension5", "", "")
                )
            )
        )
    )
)