package pt.ipc.http.models

import java.util.*

data class ConnectionRequest(
    val monitorId: UUID,
    val text: String? = null
)

data class Decision(
    val accept: Boolean
)
