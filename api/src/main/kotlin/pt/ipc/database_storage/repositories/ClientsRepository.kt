package pt.ipc.database_storage.repositories

import pt.ipc.domain.Client
import pt.ipc.domain.Role
import pt.ipc.domain.User


interface ClientsRepository {

    fun existsEmail(email : String) : Boolean

    fun getUserByToken(token: String): User?

    fun registerClient(input: Client, token: String, physicalCondition : String? = null)

    fun roleOfUser(token : String) : Role
}