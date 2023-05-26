package pt.ipc.http.models

import java.util.UUID

data class RequestInformation(
    val clientID: UUID,
    val monitorID: UUID,
    val requestID: UUID,
    val requestText: String? = null
)
