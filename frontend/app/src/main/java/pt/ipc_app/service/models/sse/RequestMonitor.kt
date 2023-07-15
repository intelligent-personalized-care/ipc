package pt.ipc_app.service.models.sse

import java.util.UUID

data class RequestMonitor(
    val requestID: UUID,
    val name: String,
    val requestText: String?,
    val clientID: UUID
): SseEvent()
