package pt.ipc.domain

import java.util.*

data class User(val id: UUID, val name: String, val email: String, val passwordHash: String, val photoID: UUID? = null)
