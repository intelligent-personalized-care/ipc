package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.Unauthenticated
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.services.ServiceUtils
import java.util.UUID

@Component
class AuthorizationHeaderProcessor(
    private val serviceUtils: ServiceUtils,
    private val jwtUtils: JwtUtils,
    private val encryptionUtils: EncryptionUtils
) {

    fun process(authorizationValue: String?): Pair<User, Role>? {
        if (authorizationValue == null) {
            return null
        }

        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }

        val token: String = parts[1]

        val (id, role, sessionID) = jwtUtils.getUserInfo(token = token)

        val user = serviceUtils.getUser(id = id, role = role, sessionID = encryptionUtils.encrypt(plainText = sessionID.toString())) ?: throw Unauthenticated

        return Pair(first = user, second = role)
    }

    fun checkIfMonitorIsVerified(monitorID: UUID) = serviceUtils.checkIfMonitorIsVerified(monitorID = monitorID)

    companion object {
        const val SCHEME = "bearer"
    }
}
