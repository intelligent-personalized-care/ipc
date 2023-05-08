package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.services.UsersServiceUtils
import javax.servlet.http.Cookie

@Component
class AuthorizationHeaderProcessor(
    private val usersServiceUtils: UsersServiceUtils
) {

    fun process(cookie: Cookie?): Pair<User, Role>? {
        if (cookie == null) return null

        val value = cookie.value

        return usersServiceUtils.getUserByToken(value)
    }
}
