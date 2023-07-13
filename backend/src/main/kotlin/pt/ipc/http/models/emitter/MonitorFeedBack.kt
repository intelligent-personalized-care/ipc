package pt.ipc.http.models.emitter

data class MonitorFeedBack(val feedBack: String) : EmitterModel(eventID = "MonitorFeedBack", obj = object { val feedBack = feedBack })
