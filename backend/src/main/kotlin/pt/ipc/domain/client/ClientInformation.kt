package pt.ipc.domain.client

import java.util.UUID

data class ClientInformation(
    val id: UUID,
    val name: String,
    val email: String
)
