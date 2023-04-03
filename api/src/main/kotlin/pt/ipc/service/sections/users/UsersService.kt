package pt.ipc.service.sections.users

import pt.ipc.domain.User
import pt.ipc.service.sections.users.dtos.RegisterInput
import pt.ipc.service.sections.users.dtos.RegisterOutput
import pt.ipc.service.sections.users.dtos.UserOutput

interface UsersService {
    fun getUser(name: String): UserOutput

    fun getUserByToken(token: String): User

    fun register(input: RegisterInput): RegisterOutput

}