package pt.ipc.http.pipeline.authentication

import org.springframework.stereotype.Component
import pt.ipc.domain.User
import pt.ipc.services.users.UsersService
import javax.servlet.http.Cookie

@Component
class AuthorizationHeaderProcessor(
   private val usersService: UsersService
) {

    fun process(cookie : Cookie?) : User?{
        if(cookie == null) return null

        val value = cookie.value

        return usersService.getUserByToken(value)
    }


}
