package pt.ipc_app.service.models.dailyList

import pt.ipc_app.service.models.exercises.ExerciseInput
import java.util.UUID

data class DailyListInput(
    val exercises: List<ExerciseInput> = listOf()
) {
    fun containsExercise(id: UUID) = exercises.any { it.exerciseInfoID == id }
    fun addExercise(exercise: ExerciseInput): DailyListInput =
        this.copy(
            exercises = exercises + exercise
        )
}