package pt.ipc.api.http.pipeline

import org.springframework.stereotype.Component
import pt.ipc.domain.User
import pt.ipc.service.sections.users.UsersServiceImpl

@Component
class AuthorizationHeaderProcessor(
    val usersService: UsersServiceImpl
) {

    fun process(authorizationValue: String?): User? {
        if (authorizationValue == null) return null

        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) return null

        if (parts[0].lowercase() != SCHEME) return null

        return usersService.getUserByToken(parts[1])
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
