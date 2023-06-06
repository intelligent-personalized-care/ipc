package pt.ipc.domain

import java.util.UUID

data class Rating(val user: UUID, val rating: Int) {
    init {
        require(rating in 1..5)
    }
}
