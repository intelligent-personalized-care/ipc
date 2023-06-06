package pt.ipc_app.domain.exercise

import java.util.*

data class ExerciseInfo(
    val id: UUID,
    val title: String,
    val description: String,
    val type: ExerciseType
)

enum class ExerciseType {
    Legs,
    Byceps,
    Shoulders,
    Back,
    Gluts,
    Abdominals,
    Chest
}
