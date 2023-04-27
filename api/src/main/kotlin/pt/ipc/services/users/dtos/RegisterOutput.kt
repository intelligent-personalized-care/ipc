package pt.ipc.services.users.dtos

import java.util.UUID

data class RegisterOutput(
    val id: UUID,
    val token: String
)
