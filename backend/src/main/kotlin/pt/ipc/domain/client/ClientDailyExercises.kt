package pt.ipc.domain.client

import pt.ipc.domain.exercises.DailyExercise
import java.util.UUID

data class ClientDailyExercises(
    val id: UUID,
    val name: String,
    val planId: Int,
    val dailyListId: Int,
    val exercises: List<DailyExercise>
)
