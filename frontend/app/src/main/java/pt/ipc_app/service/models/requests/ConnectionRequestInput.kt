package pt.ipc_app.service.models.requests

import java.util.*

data class ConnectionRequestInput(
    val clientID: UUID,
    val text: String? = null
)