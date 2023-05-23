package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Exercise(
    val id: Int,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int = 0,
    val reps: Int = 0
): Parcelable {
    fun isDone() = Random.nextBoolean()
}