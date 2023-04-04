package pt.ipc.services.users

import pt.ipc.domain.User
import pt.ipc.services.users.dtos.RegisterInput
import pt.ipc.services.users.dtos.RegisterOutput

interface UsersService {

    fun getUserByToken(token: String): User?

    fun register(input: RegisterInput): RegisterOutput

}