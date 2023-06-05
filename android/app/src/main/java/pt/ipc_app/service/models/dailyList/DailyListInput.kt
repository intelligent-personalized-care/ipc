package pt.ipc_app.service.models.dailyList

import pt.ipc_app.domain.exercise.Exercise
import java.util.UUID

data class DailyListInput(
    val exercises: List<Exercise> = listOf()
) {
    fun containsExercise(id: UUID) = exercises.any { it.exerciseInfoID == id }
    fun addExercise(exercise: Exercise): DailyListInput =
        this.copy(
            exercises = exercises + exercise
        )
}