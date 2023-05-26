package pt.ipc.http.models

import java.util.*

data class ConnectionRequestInput(
    val monitorId: UUID,
    val text: String? = null
)

data class ConnectionRequestDecisionInput(
    val accept: Boolean
)
