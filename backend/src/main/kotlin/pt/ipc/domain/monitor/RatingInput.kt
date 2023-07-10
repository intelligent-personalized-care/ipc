package pt.ipc.domain.monitor

import pt.ipc.domain.exceptions.BadRating
import java.util.UUID

data class RatingInput(val user: UUID, val rating: Int) {
    init {
        if (rating !in 1..5) throw BadRating
    }
}
