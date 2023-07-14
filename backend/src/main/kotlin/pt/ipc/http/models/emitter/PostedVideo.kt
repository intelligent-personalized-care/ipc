package pt.ipc.http.models.emitter

import java.util.UUID

data class PostedVideo(val clientID : UUID, val name: String, val exerciseID : Int) : EmitterModel(
    eventID = "PostedVideo",
    obj = object {
        val clientID = clientID
        val name = name
        val exerciseID = exerciseID
    }
)
