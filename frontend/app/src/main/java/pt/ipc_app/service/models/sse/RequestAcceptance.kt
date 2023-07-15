package pt.ipc_app.service.models.sse

import pt.ipc_app.service.models.users.MonitorOutput

data class RequestAcceptance(
    val monitor: MonitorOutput
): SseEvent()