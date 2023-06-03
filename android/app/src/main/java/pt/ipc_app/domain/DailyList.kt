package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.ipc_app.domain.exercise.DailyExercise

@Parcelize
data class DailyList(
    val exercises: List<DailyExercise>
) : Parcelable