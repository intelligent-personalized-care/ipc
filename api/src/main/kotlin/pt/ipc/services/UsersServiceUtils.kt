package pt.ipc.services

import org.springframework.stereotype.Component
import pt.ipc.domain.BadEmail
import pt.ipc.domain.MonitorNotVerified
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.WeakPassword
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Component
class UsersServiceUtils(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager: TransactionManager,
    private val jwtUtils: JwtUtils
) {

    fun getUserByToken(token: String): Pair<User, Role>? {
        val hashedToken = encryptionUtils.encrypt(token)
        return transactionManager.runBlock(
            block = {
                val (user, role) = it.clientsRepository.getUserByToken(token = hashedToken) ?: return@runBlock null
                if (role == Role.MONITOR) {
                    if (!it.monitorRepository.checkIfMonitorIsVerified(user.id)) throw MonitorNotVerified
                }
                Pair(user, role)
            }
        )
    }

    fun createCredentials(email: String, role: Role): Pair<String, UUID> {
        val id = UUID.randomUUID()

        val token = jwtUtils.createJWToken(email = email, id = id, role = role)

        return Pair(token.token, id)
    }

    private fun isPasswordSafe(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$")
        return regex.matches(password)
    }

    fun checkDetails(email: String, password: String) {
        if (!email.contains("@")) throw BadEmail
        if (!isPasswordSafe(password = password)) throw WeakPassword
    }
}
