package pt.ipc.service.sections.users

import org.springframework.stereotype.Component
import pt.ipc.domain.User
import pt.ipc.service.sections.users.dtos.RegisterInput
import pt.ipc.service.sections.users.dtos.RegisterOutput
import pt.ipc.service.sections.users.dtos.UserOutput

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