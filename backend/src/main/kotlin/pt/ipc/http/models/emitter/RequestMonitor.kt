package pt.ipc.http.models.emitter

import java.util.UUID

class RequestMonitor(val requestID: UUID, val name: String) : EmitterModel(
    eventID = "RequestMonitor",
    object  {
                val requestID = requestID
                val name = name
    }
)

