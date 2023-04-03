package pt.ipc.repository.sections.users

import java.time.Instant
import java.util.UUID

interface UsersRepository {
    fun storeUser(
        username: String,
        passwordValidation: PasswordValidationInfo,
        score:Int
    ): String

    fun getUserByUsername(username: String): User?

    fun getUserById(id: Int): User?

    fun removeFromLobby(id: Int)

    fun getUserByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): User?

    fun isUserStoredByUsername(username: String): Boolean

    fun createToken(userId: Int, token: TokenValidationInfo)

    fun updateTokenLastUsed(token: Token, now: Instant)

    fun joinLobby(userId: Int/*,username: String*/): Game?

    fun updateUser(userId: Int,score: Int)

    fun getLeaderBoard():List<User>

    fun isUserInLobby(userId: Int): Boolean

    fun loginUser(username: String, passwordValidation: PasswordValidationInfo): Pair<String?, String>?

    fun checkUserWaitStatus(username: String): UUID?
}