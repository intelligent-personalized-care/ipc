package pt.ipc_app.service.models.dailyList

import pt.ipc_app.domain.exercise.Exercise
import java.util.UUID

data class DailyListInput(
    val exercises: MutableList<Exercise> = mutableListOf()
) {
    fun containsExercise(id: UUID) = exercises.any { it.exerciseInfoID == id }
    fun addExercise(exercise: Exercise) = exercises.add(exercise)
}