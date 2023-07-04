package pt.ipc_app.service.models.users

import java.util.*

data class RatingInput(
    val user: UUID,
    val rating: Int
)
