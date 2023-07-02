package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
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

    fun getUser(id: UUID, role: Role): User? =
        when (role) {
            Role.MONITOR -> transactionManager.runBlock(
                block = {
                    it.monitorRepository.getUserByID(id = id)
                }
            )
            Role.CLIENT -> transactionManager.runBlock(
                block = {
                    it.clientsRepository.getUserByID(id = id)
                }
            )
            Role.ADMIN -> transactionManager.runBlock(
                block = {
                    it.adminRepository.getUserByID(id = id)
                }
            )
        }

    fun checkIfMonitorIsVerified(monitorID: UUID) =
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
