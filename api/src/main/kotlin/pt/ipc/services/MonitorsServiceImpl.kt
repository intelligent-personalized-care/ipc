package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.*
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.http.models.RequestInformation
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

    override fun getMonitor(monitorID: UUID): MonitorDetails =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getMonitor(monitorID) ?: throw MonitorNotFound
            }
        )

    override fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.searchMonitorsAvailable(name, skip, limit)
            }
        )

    override fun updateProfilePicture(monitorID: UUID, photo: ByteArray) {
        val photoID = UUID.randomUUID()

        transactionManager.runBlock(
            block = {
                it.clientsRepository.updateProfilePictureID(userID = monitorID, profileID = photoID)
                it.cloudStorage.uploadProfilePicture(fileName = photoID, file = photo)
            }
        )
    }

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.monitorRequests(monitorID = monitorID)
            }
        )

    override fun decideRequest(requestID: UUID, monitorID: UUID, accept: Boolean) {
        transactionManager.runBlock(
            block = {

                val requestInformation = it.monitorRepository.getRequestInformation(requestID = requestID) ?: throw RequestNotExists

                if(requestInformation.monitorID != monitorID ) throw Unauthorized

                it.monitorRepository.decideRequest(
                    requestID = requestID,
                    clientID = requestInformation.clientID,
                    monitorID = monitorID,
                    accept = accept
                )
            }
        )
    }

    override fun createPlan(monitorID: UUID, clientID: UUID, plan: Plan): Int {
        return transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.checkIfIsMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient
                it.exerciseRepository.createPlan(monitorID = monitorID, clientID = clientID, plan = plan)
            }
        )
    }

    override fun getPlan(monitorID: UUID, planID: Int): PlanOutput {
        return transactionManager.runBlock(
            block = {
                if (!it.exerciseRepository.checkIfPlanIsOfMonitor(monitorID = monitorID, planID = planID)) throw NotPlanOfMonitor
                it.exerciseRepository.getPlan(planID)
            }
        )
    }
}
