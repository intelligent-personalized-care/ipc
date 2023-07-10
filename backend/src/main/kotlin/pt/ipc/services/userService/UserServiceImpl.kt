package pt.ipc.services.userService

import org.springframework.stereotype.Service
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.LoginFailed
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.domain.jwt.PairOfTokens
import pt.ipc.services.ServiceUtils
import pt.ipc.services.dtos.LoginOutput
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class UserServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val serviceUtils: ServiceUtils
) : UserService {

    override fun getUserPhoto(userID: UUID): ByteArray =
        transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadProfilePicture(fileName = userID)
            }
        )

    override fun login(email: String, password: String): LoginOutput =
        transactionManager.runBlock(
            block = {
                val userID = it.usersRepository.login(email = email, passwordHash = encryptionUtils.encrypt(plainText = password)) ?: throw LoginFailed

                val user = it.usersRepository.getUserByID(userID = userID) ?: throw UserNotExists

                val role = it.usersRepository.getRoleByID(userID = user.id)

                val sessionID = UUID.randomUUID()

                val (accessToken, refreshToken) = serviceUtils.createTokens(id = user.id, role = role, sessionID = sessionID)

                it.usersRepository.updateSession(userID = userID, sessionID = encryptionUtils.encrypt(plainText = sessionID.toString()))

                LoginOutput(id = user.id, accessToken = accessToken, refreshToken = refreshToken, name = user.name, role = role)
            }
        )

    override fun refreshToken(refreshToken: String): PairOfTokens =
        transactionManager.runBlock(
            block = {
                val sessionID = serviceUtils.getSessionID(refreshToken = refreshToken)

                val userID = it.usersRepository.getUserBySession(sessionID = encryptionUtils.encrypt(plainText = sessionID.toString())) ?: throw UserNotExists

                val role = it.usersRepository.getRoleByID(userID = userID)

                val newSessionID = UUID.randomUUID()

                it.usersRepository.updateSession(userID = userID, sessionID = encryptionUtils.encrypt(plainText = newSessionID.toString()))

                val (newAccessToken, newRefreshToken) = serviceUtils.createTokens(id = userID, role = role, sessionID = newSessionID)

                PairOfTokens(accessToken = newAccessToken, refreshToken = newRefreshToken)
            }
        )
}
