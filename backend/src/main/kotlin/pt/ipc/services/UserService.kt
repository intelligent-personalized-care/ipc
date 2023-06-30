package pt.ipc.services

import pt.ipc.services.dtos.CredentialsOutput
import java.util.*

interface UserService {

    fun getUserPhoto(userID : UUID) : ByteArray

    fun login(email : String, password : String) : CredentialsOutput

}