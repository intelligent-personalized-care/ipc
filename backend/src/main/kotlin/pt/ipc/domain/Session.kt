package pt.ipc.domain

import java.util.UUID

data class Session(val userID: UUID, val accessToken: String, val refreshToken: String, val sessionID: UUID)
