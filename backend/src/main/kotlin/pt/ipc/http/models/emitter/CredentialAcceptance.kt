package pt.ipc.http.models.emitter

data class CredentialAcceptance(val acceptance: Boolean) : EmitterModel(eventID = "CredentialAcceptance", obj = object { val acceptance = acceptance })
