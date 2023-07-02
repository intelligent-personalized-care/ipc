package pt.ipc.http.models

import java.util.*

data class MonitorOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating
)

data class Rating(val averageStarts: Float, val nrOfReviews: Int) {
    fun isEmpty(): Rating = if (nrOfReviews == 0) Rating(5F, 0) else this
}

data class MonitorProfile(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating,
    val docState: String
)
