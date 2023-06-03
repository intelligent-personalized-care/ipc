package pt.ipc_app.domain.exercise

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.random.Random

data class Exercise(
    val exerciseInfoID: UUID,
    val sets: Int,
    val reps: Int
)

@Parcelize
data class DailyExercise(
    val id: Int,
    val exerciseInfoID: UUID,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int = 0,
    val reps: Int = 0
): Parcelable {
    fun isDone() = Random.nextBoolean()
}