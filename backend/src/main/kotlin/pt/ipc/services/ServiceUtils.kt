package pt.ipc.services

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.Session
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.BadEmail
import pt.ipc.domain.exceptions.MonitorNotVerified
import pt.ipc.domain.exceptions.WeakPassword
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Component
class ServiceUtils(
    private val transactionManager: TransactionManager,
    private val jwtUtils: JwtUtils
) {
    companion object {
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
        private const val EMAIL_REGEX = "^[A-Za-z\\d+_.-]+@(.+)$"
    }

    fun getUser(id: UUID, role: Role, sessionID: String): User? =
        when (role) {
            Role.MONITOR -> transactionManager.run {
                it.monitorRepository.getUserByIDAndSession(id = id, sessionID = sessionID)
            }

            Role.CLIENT -> transactionManager.run {
                it.usersRepository.getUserByIDAndSession(id = id, sessionID = sessionID)
            }

            Role.ADMIN -> transactionManager.run {
                it.adminRepository.getUserByIDAndSession(id = id, sessionID = sessionID)
            }
        }

    fun checkIfMonitorIsVerified(monitorID: UUID) =
        transactionManager.run {
            if (!it.monitorRepository.checkIfMonitorIsVerified(monitorID = monitorID)) throw MonitorNotVerified
        }

    fun createCredentials(role: Role): Session {
        val userID = UUID.randomUUID()

        val sessionID = UUID.randomUUID()

        val (accessToken, refreshToken) = createTokens(id = userID, role = role, sessionID = sessionID)

        return Session(userID = userID, accessToken = accessToken, refreshToken = refreshToken, sessionID = sessionID)
    }

    fun createTokens(id: UUID, role: Role, sessionID: UUID): Pair<String, String> {
        return Pair(
            jwtUtils.createAccessToken(userID = id, role = role, sessionID = sessionID),
            jwtUtils.createRefreshToken(session = sessionID)
        )
    }

    fun getSessionID(refreshToken: String): UUID = jwtUtils.getSessionID(token = refreshToken)

    fun isValidEmail(email: String) = email.matches(Regex(EMAIL_REGEX))

    private fun isPasswordSafe(password: String) = password.matches(Regex(PASSWORD_REGEX))

    fun checkDetails(email: String, password: String) {
        if (!isValidEmail(email)) throw BadEmail
        if (!isPasswordSafe(password)) throw WeakPassword
    }
}
