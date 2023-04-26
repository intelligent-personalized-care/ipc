package pt.ipc.domain

import java.util.UUID

data class RequestInformation(val clientID : UUID, val monitorID : UUID, val requestID : UUID)
