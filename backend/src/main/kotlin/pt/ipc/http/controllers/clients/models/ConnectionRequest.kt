package pt.ipc.http.controllers.clients.models

import java.util.UUID

data class ConnectionRequest(
    val clientID: UUID,
    val text: String? = null
)
