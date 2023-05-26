package pt.ipc.http.models

import java.util.*

data class ConnectionRequestInput(
    val clientId: UUID
)

data class ConnectionRequestDecisionInput(
    val accept: Boolean
)
