package pt.ipc.storage.repositories

import pt.ipc.domain.Role
import pt.ipc.domain.User
import java.util.*

interface UsersRepository {

    fun login(email: String, passwordHash: String): UUID?

    fun getUserByID(userID: UUID): User?

    fun getRoleByID(userID: UUID): Role

    fun getUserBySession(sessionID: String): UUID?

    fun updateSession(userID: UUID, sessionID: String)

    fun getUserByIDAndSession(id: UUID, sessionID: String): User?
}
