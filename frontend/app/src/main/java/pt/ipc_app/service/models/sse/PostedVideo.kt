package pt.ipc_app.service.models.sse

import java.util.*

data class PostedVideo(
    val clientID: UUID,
    val name: String,
    val exerciseID: Int
): SseEvent()