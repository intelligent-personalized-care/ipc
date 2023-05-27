package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.services.UsersServiceUtils

@Component
class AuthorizationHeaderProcessor(
    private val usersServiceUtils: UsersServiceUtils
) {

    fun process(authorizationValue : String?): Pair<User, Role>? {

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

        return usersServiceUtils.getUserByToken(parts[1])
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
