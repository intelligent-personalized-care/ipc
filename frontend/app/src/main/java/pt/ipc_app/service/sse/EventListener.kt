package pt.ipc_app.service.sse

import pt.ipc_app.service.models.sse.SseEvent

interface SseEventListener {
    fun onEventReceived(eventData: SseEvent)
}
