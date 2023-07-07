package pt.ipc.services

import pt.ipc.http.models.LoginOutput
import java.util.*

interface UserService {

    fun getUserPhoto(userID: UUID): ByteArray

    fun login(email: String, password: String) : LoginOutput
}
