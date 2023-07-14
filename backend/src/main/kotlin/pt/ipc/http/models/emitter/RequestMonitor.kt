package pt.ipc.http.models.emitter

import java.util.UUID

data class RequestMonitor(val requestID: UUID, val name: String, val requestText : String?, val clientID : UUID) : EmitterModel(
    eventID = "RequestMonitor",
    object {
        val requestID = requestID
        val name = name
        val requestText = requestText
        val clientID = clientID
    }
)
