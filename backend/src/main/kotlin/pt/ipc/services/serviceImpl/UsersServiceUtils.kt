package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.BadEmail
import pt.ipc.domain.exceptions.MonitorNotVerified
import pt.ipc.domain.exceptions.WeakPassword
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Component
class UsersServiceUtils(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager: TransactionManager,
    private val jwtUtils: JwtUtils
) {
    companion object {
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
        private const val EMAIL_REGEX = "^[A-Za-z\\d+_.-]+@(.+)$"
    }

    fun getUserByToken(token: String): Pair<User, Role>? {
        val hashedToken = encryptionUtils.encrypt(token)
        return transactionManager.runBlock(
            block = {
                val (user, role) = it.clientsRepository.getUserByToken(token = hashedToken) ?: return@runBlock null
                Pair(user, role)
            }
        )
    }

    fun checkIfMonitorIsVerified(monitorID : UUID) =
        transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.checkIfMonitorIsVerified(monitorID = monitorID)) throw MonitorNotVerified
            }
        )

    fun createCredentials(role: Role): Pair<String, UUID> {
        val id = UUID.randomUUID()

        val token = jwtUtils.createJWToken(id = id, role = role)

        return Pair(token.token, id)
    }

    fun isValidEmail(email: String) = email.matches(Regex(EMAIL_REGEX))

    private fun isPasswordSafe(password: String) = password.matches(Regex(PASSWORD_REGEX))

    fun checkDetails(email: String, password: String) {
        if (!isValidEmail(email)) throw BadEmail
        if (!isPasswordSafe(password)) throw WeakPassword
    }
}
