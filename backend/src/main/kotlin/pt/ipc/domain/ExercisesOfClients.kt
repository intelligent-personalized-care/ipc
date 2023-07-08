package pt.ipc.domain

import java.util.*

data class ExercisesOfClients(val clientsExercises: List<ClientDailyExercises>)

data class ClientDailyExercises(
    val id: UUID,
    val name: String,
    val planId: Int,
    val dailyListId: Int,
    val exercises: List<DailyExercise>
)
