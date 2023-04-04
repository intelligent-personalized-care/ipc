package pt.ipc.repositories

import pt.ipc.domain.User
import pt.ipc.services.users.dtos.RegisterOutput


interface UsersRepository {

    fun existsEmail(email : String) : Boolean

    fun getUserByToken(token: String): User?

    fun register(input: User, token: String, physicalCondition : String)
}