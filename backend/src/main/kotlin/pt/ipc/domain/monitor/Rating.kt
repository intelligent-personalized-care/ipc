package pt.ipc.domain.monitor

data class Rating(val averageStarts: Float, val nrOfReviews: Int) {
    fun isEmpty(): Rating = if (nrOfReviews == 0) Rating(5F, 0) else this
}
