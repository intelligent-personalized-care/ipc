package pt.ipc.domain.plan

import pt.ipc.domain.exercises.DailyExercise
import pt.ipc.domain.exercises.Exercise

data class DailyListInput(val exercises: List<Exercise>)

data class DailyListOutput(
    val id: Int,
    val exercises: List<DailyExercise>
)
