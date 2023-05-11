package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Exercise(
    val id: Int,
    val title: String,
    val reps: Int = 0,
    val sets: Int = 0
): Parcelable {
    fun isDone() = Random.nextBoolean()
}