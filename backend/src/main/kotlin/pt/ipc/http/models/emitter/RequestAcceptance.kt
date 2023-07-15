package pt.ipc.http.models.emitter

import pt.ipc.services.dtos.MonitorOutput

data class RequestAcceptance(val monitor : MonitorOutput) : EmitterModel()
