package pt.ipc_app.service.models.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class MonitorOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating,
    val requested: Boolean = true,
    val isMyMonitor: Boolean = false
): Parcelable

@Parcelize
data class Rating(
    val averageStarts: Float,
    val nrOfReviews: Int
): Parcelable

data class ListMonitorsOutput(
    val monitors: List<MonitorOutput>
)