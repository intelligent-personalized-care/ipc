package pt.ipc.services.users

import org.springframework.stereotype.Service
import pt.ipc.database_storage.artificialTransaction.TransactionManager
import pt.ipc.domain.User
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.services.users.dtos.RegisterMonitorInput
import pt.ipc.services.users.dtos.RegisterOutput
import java.time.LocalDate

@Service
class MonitorsServiceImpl(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager : TransactionManager,
    private val usersServiceUtils: UsersServiceUtils
) : MonitorService {

    override fun registerMonitor(registerMonitorInput: RegisterMonitorInput): RegisterOutput {

        val (token,userID) = usersServiceUtils.createCredentials(registerMonitorInput.email,registerMonitorInput.name)

        val encryptedToken = encryptionUtils.encrypt(token)

        val user = User(
            id = userID,
            name = registerMonitorInput.name,
            email = registerMonitorInput.email,
            passwordHash = encryptionUtils.encrypt(registerMonitorInput.password)
        )

        usersServiceUtils.checkDetails(email = registerMonitorInput.email, password = registerMonitorInput.password)

        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadMonitorCredentials(
                    fileName = userID,
                    file = registerMonitorInput.credential
                )
                it.monitorRepository.registerMonitor(user = user, date = LocalDate.now(), encryptedToken = encryptedToken)
            },
            fileName = userID
        )

        return RegisterOutput(id = userID, token = token)

    }
}