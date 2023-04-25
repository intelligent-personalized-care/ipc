package pt.ipc.database_storage.repositories

import pt.ipc.domain.Client
import pt.ipc.domain.Role
import pt.ipc.domain.User
import java.util.*


interface ClientsRepository {

    fun existsEmail(email : String) : Boolean

    fun getUserByToken(token: String) : Pair<User,Role>?

    fun registerClient(input: Client, token: String, physicalCondition : String? = null)

    fun updateProfilePictureID(userID : UUID, profileID : UUID)

    fun roleOfUser(id : UUID) : Role

}