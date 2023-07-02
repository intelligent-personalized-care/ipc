package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.LoginFailed
import pt.ipc.services.UserService
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class UserServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils
) : UserService {

    override fun getUserPhoto(userID: UUID): ByteArray =
        transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadProfilePicture(fileName = userID)
            }
        )

    override fun login(email: String, password: String): CredentialsOutput =
        transactionManager.runBlock(
            block = {
                it.clientsRepository.login(email = email, passwordHash = encryptionUtils.encrypt(plainText = password)) ?: throw LoginFailed
            }
        )
}
