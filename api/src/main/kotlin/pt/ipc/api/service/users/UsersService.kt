package pt.ipc.api.service.users

import pt.ipc.api.domain.User
import pt.ipc.api.service.users.dtos.RegisterInput
import pt.ipc.api.service.users.dtos.RegisterOutput
import pt.ipc.api.service.users.dtos.UserOutput

interface UsersService {
    fun getUser(name: String): UserOutput

    fun getUserByToken(token: String): User

    fun register(input: RegisterInput): RegisterOutput

}