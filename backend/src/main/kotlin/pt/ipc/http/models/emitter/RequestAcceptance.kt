package pt.ipc.http.models.emitter

data class RequestAcceptance(val monitorName: String) : EmitterModel(eventID = "RequestAcceptance", obj = object { val monitorName = monitorName })
