package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.LoginFailed
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.http.models.LoginOutput
import pt.ipc.services.UserService
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
                val credentialsOutput = it.clientsRepository.login(email = email, passwordHash = encryptionUtils.encrypt(plainText = password)) ?: throw LoginFailed

                val user = it.clientsRepository.getUserByID(id = credentialsOutput.id) ?: throw UserNotExists

                val role = it.clientsRepository.getRoleByID(userID = user.id)

                val newToken = serviceUtils.createToken(id = user.id, role = role)

                it.clientsRepository.updateToken(userID = user.id, encryptionUtils.encrypt(plainText = newToken))

                LoginOutput(id = user.id, token = newToken, name = user.name, role = role  )

            }
        )
}
