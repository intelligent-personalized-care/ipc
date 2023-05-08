package pt.ipc_app.domain

import kotlin.random.Random

data class Exercise(
    val id: Int,
    val title: String,
    val reps: Int = 0,
    val sets: Int = 0
) {
    fun isDone() = Random.nextBoolean()
}