package pt.ipc.api.service.users

import org.springframework.stereotype.Component
import pt.ipc.api.domain.User
import pt.ipc.api.service.users.dtos.RegisterInput
import pt.ipc.api.service.users.dtos.RegisterOutput
import pt.ipc.api.service.users.dtos.UserOutput

@Component
class UsersServiceImpl(
): UsersService {
    override fun getUser(name: String): UserOutput {
        TODO("Not yet implemented")
    }

    override fun getUserByToken(token: String): User {
        TODO("Not yet implemented")
    }

    override fun register(input: RegisterInput): RegisterOutput {
        TODO("Not yet implemented")
    }

}