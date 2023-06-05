package pt.ipc.http.models

import java.util.*

data class MonitorOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating
)

data class Rating(val starsAverage : Float, val nrOfReviews : Int)