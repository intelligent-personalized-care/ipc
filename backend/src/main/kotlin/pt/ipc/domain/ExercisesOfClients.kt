package pt.ipc.domain

import java.util.*

data class ExercisesOfClients(val clientsExercises : List<ClientExercises>)

data class ClientExercises(val id : UUID, val name : String, val exercises : List<ExerciseTotalInfo>)
