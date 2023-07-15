package pt.ipc.http.models.emitter

import java.util.UUID

data class RequestMonitor(val requestID: UUID, val name: String, val requestText: String?, val clientID: UUID) : EmitterModel()
