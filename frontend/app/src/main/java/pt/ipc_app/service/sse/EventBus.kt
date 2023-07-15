package pt.ipc_app.service.sse

import pt.ipc_app.service.models.sse.SseEvent

object EventBus {
    // List of registered listeners for events
    private val listeners: MutableList<SseEventListener> = mutableListOf()

    /**
     * Registers a listener to receive events
     * @param listener The listener to be registered
     */
    fun registerListener(listener: SseEventListener) {
        listeners.add(listener)
    }

    /**
     * Unregisters a listener
     * @param listener The listener to be removed
     */
    fun unregisterListener(listener: SseEventListener) {
        listeners.remove(listener)
    }

    /**
     * Posts an event to all registered listeners
     * @param eventData The event to be posted
     */
    fun postEvent(eventData: SseEvent) {
        for (listener in listeners) {
            listener.onEventReceived(eventData)
        }
    }
}

