package pt.ipc_app.service.models.exercises

import java.util.*

data class ExerciseInput(
    val exerciseInfoID: UUID,
    val sets: Int,
    val reps: Int
)
