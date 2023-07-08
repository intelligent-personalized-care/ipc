package pt.ipc_app.service.models.exercises

import pt.ipc_app.domain.exercise.DailyExercise
import java.util.*

data class ExercisesOfClients(
    val clientsExercises: List<ClientExercises>
)

data class ClientExercises(
    val id: UUID,
    val name: String,
    val exercises: List<DailyExercise>
) {
    fun allExercisesDone() = exercises.all { it.isDone }
}
