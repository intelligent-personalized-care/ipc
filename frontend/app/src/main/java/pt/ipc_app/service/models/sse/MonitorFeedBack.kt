package pt.ipc_app.service.models.sse

data class MonitorFeedBack(
    val feedBack: String
): SseEvent()