package pt.ipc_app.service.models

import java.util.*

data class ConnectionRequestInput(
    val monitorId: UUID,
    val text: String? = null
)