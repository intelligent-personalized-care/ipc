package pt.ipc.services

import pt.ipc.domain.jwt.PairOfTokens
import pt.ipc.http.models.LoginOutput
import java.util.*

interface UserService {

    fun getUserPhoto(userID: UUID): ByteArray

    fun login(email: String, password: String): LoginOutput

    fun refreshToken(refreshToken: String): PairOfTokens
}
