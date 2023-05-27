package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyList(
    val exercises: List<Exercise>
) : Parcelable