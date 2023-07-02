package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.services.serviceImpl.ServiceUtils
import java.util.UUID

@Component
class AuthorizationHeaderProcessor(
    private val serviceUtils: ServiceUtils,
    private val jwtUtils: JwtUtils
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

        val (id, role) = jwtUtils.getUserInfo(token = token)

        val user = serviceUtils.getUser(id = id, role = role) ?: throw UserNotExists

        return Pair(first = user, second = role)
    }

    fun checkIfMonitorIsVerified(monitorID: UUID) = serviceUtils.checkIfMonitorIsVerified(monitorID = monitorID)

    companion object {
        const val SCHEME = "bearer"
    }
}
