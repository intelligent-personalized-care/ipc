package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.Role
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.User
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.services.dtos.RegisterMonitorInput
import pt.ipc.services.dtos.RegisterOutput
import pt.ipc.storage.transaction.TransactionManager
import java.time.LocalDate
import java.util.*

@Service
class MonitorsServiceImpl(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager: TransactionManager,
    private val usersServiceUtils: UsersServiceUtils
) : MonitorService {

    override fun registerMonitor(registerMonitorInput: RegisterMonitorInput): RegisterOutput {
        val (token, userID) = usersServiceUtils.createCredentials(email = registerMonitorInput.email, role = Role.MONITOR)

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

    override fun updateProfilePicture(monitorID: UUID, photo: ByteArray) {
        val photoID = UUID.randomUUID()

        transactionManager.runBlock(
            block = {
                it.clientsRepository.updateProfilePictureID(userID = monitorID, profileID = photoID)
                it.cloudStorage.uploadProfilePicture(fileName = photoID, file = photo)
            }
        )
    }

    override fun requestClient(monitorID: UUID, clientID: UUID): UUID {
        val requestID = UUID.randomUUID()

        transactionManager.runBlock(
            block = {
                if (
                    it.clientsRepository.roleOfUser(monitorID) != Role.MONITOR ||
                    it.clientsRepository.roleOfUser(clientID) != Role.CLIENT
                ) {
                    throw Unauthorized
                }

                it.monitorRepository.requestClient(requestID = requestID, monitorID = monitorID, clientID = clientID)
            }
        )

        return requestID
    }

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.monitorRequests(monitorID = monitorID)
            }
        )
}
