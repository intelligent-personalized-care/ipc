package pt.ipc_app.domain.exercise

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class Exercise(
    val exeID: UUID,
    val exeTitle: String,
    val exeDescription: String,
    val exeSets: Int,
    val exeReps: Int
): Parcelable

@Parcelize
data class FreeExercise(
    val exerciseInfoID: UUID,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int = 0,
    val reps: Int = 0
): Exercise(exerciseInfoID, title, description, sets, reps), Parcelable

@Parcelize
data class ExerciseTotalInfo(
    val planId: Int,
    val dailyListId: Int,
    val exercise: DailyExercise
): Exercise(exercise.exerciseInfoID, exercise.title, exercise.description, exercise.sets, exercise.reps), Parcelable

@Parcelize
data class DailyExercise(
    val id: Int,
    val exerciseInfoID: UUID,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int = 0,
    val reps: Int = 0,
    val isDone: Boolean = false
): Parcelable