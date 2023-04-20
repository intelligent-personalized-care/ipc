package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.jwt.JwtUtils
import pt.ipc.services.users.UsersServiceUtils
import javax.servlet.http.Cookie

@Component
class AuthorizationHeaderProcessor(
   private val usersServiceUtils: UsersServiceUtils,
   private val jwtUtils: JwtUtils
) {

    fun process(cookie : Cookie?) : Pair<User,Role>?{
        if(cookie == null) return null

        val value = cookie.value

        val role = jwtUtils.getRoleFromToken(value)

        val user = usersServiceUtils.getUserByToken(value) ?: return null

        return Pair(user,role)
    }


}
