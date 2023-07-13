package pt.ipc.http.models.emitter

data class PostedVideo(val name: String) : EmitterModel(eventID = "PostedVideo", obj = object { val name = name })
