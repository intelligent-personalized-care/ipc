package pt.ipc.services.monitorService

import org.springframework.stereotype.Service
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.client.ClientDailyExercises
import pt.ipc.domain.client.ClientInformation
import pt.ipc.domain.client.ClientOfMonitor
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.ClientAlreadyHavePlanInThisPeriod
import pt.ipc.domain.exceptions.HasNotUploadedVideo
import pt.ipc.domain.exceptions.MonitorNotFound
import pt.ipc.domain.exceptions.NotMonitorOfClient
import pt.ipc.domain.exceptions.NotPlanOfMonitor
import pt.ipc.domain.exceptions.PlanNotFound
import pt.ipc.domain.exceptions.RequestNotExists
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.domain.monitor.MonitorDetails
import pt.ipc.domain.monitor.MonitorProfile
import pt.ipc.domain.monitor.RequestInformation
import pt.ipc.domain.plan.PlanInfoOutput
import pt.ipc.domain.plan.PlanInput
import pt.ipc.domain.plan.PlanOutput
import pt.ipc.services.ServiceUtils
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorOutput
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.storage.transaction.TransactionManager
import java.time.LocalDate
import java.util.*

@Service
class MonitorsServiceImpl(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager: TransactionManager,
    private val serviceUtils: ServiceUtils
) : MonitorService {

    override fun registerMonitor(registerInput: RegisterInput): CredentialsOutput {
        serviceUtils.checkDetails(email = registerInput.email, password = registerInput.password)

        val (userID, accessToken, refreshToken, sessionID) = serviceUtils.createCredentials(role = Role.MONITOR)

        val user = User(
            id = userID,
            name = registerInput.name,
            email = registerInput.email,
            passwordHash = encryptionUtils.encrypt(registerInput.password)
        )

        val encryptedSession = encryptionUtils.encrypt(plainText = sessionID.toString())

        transactionManager.runBlock(
            block = {
                it.monitorRepository.registerMonitor(user = user, sessionID = encryptedSession)
            }
        )

        return CredentialsOutput(id = userID, accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun insertCredential(monitorID: UUID, credential: ByteArray) {
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadMonitorCredentials(fileName = monitorID, file = credential)
                it.monitorRepository.insertCredential(monitorID = monitorID, dtSubmit = LocalDate.now())
            }
        )
    }

    override fun getMonitor(monitorID: UUID): MonitorDetails =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getMonitor(monitorID = monitorID) ?: throw MonitorNotFound
            }
        )

    override fun getClientsOfMonitor(monitorID: UUID): List<ClientInformation> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getClientsOfMonitor(monitorID = monitorID)
            }
        )

    override fun getClientOfMonitor(monitorID: UUID, clientID: UUID): ClientOfMonitor =
        transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.isMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient
                it.monitorRepository.getClientOfMonitor(monitorID = monitorID, clientID = clientID) ?: throw UserNotExists
            }
        )

    override fun updateProfilePicture(monitorID: UUID, photo: ByteArray) {
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = monitorID, file = photo)
            }
        )
    }

    override fun getMonitorProfile(monitorID: UUID): MonitorProfile =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getMonitorProfile(monitorID = monitorID) ?: throw UserNotExists
            }
        )

    override fun getProfilePicture(monitorID: UUID): ByteArray {
        return transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadProfilePicture(fileName = monitorID)
            }
        )
    }

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.monitorRequests(monitorID = monitorID)
            }
        )

    override fun decideRequest(requestID: UUID, monitorID: UUID): Triple<List<ClientInformation>, UUID, MonitorOutput> =
        transactionManager.runBlock(
            block = {
                val requestInformation = it.monitorRepository.getRequestInformation(requestID = requestID) ?: throw RequestNotExists

                    it.monitorRepository.decideRequest(
                        requestID = requestID,
                        clientID = requestInformation.clientID,
                        monitorID = monitorID
                    )

                val clients = it.monitorRepository.getClientsOfMonitor(monitorID = monitorID)

                val details = it.monitorRepository.getMonitorOfClient(requestInformation.clientID) ?: throw MonitorNotFound
                val stars = it.monitorRepository.getMonitorRating(details.id)

                val monitorOutput = MonitorOutput(id = details.id, name = details.name, email = details.email, rating = stars)

                Triple(first = clients, second = requestInformation.clientID, third = monitorOutput)
            }
        )

    override fun deleteConnection(monitorID: UUID, clientID: UUID) {
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getClientOfMonitor(monitorID = monitorID, clientID = clientID) ?: NotMonitorOfClient
                it.clientsRepository.deleteConnection(monitorID = monitorID, clientID = clientID)
            }
        )
    }


    override fun createPlan(monitorID: UUID, planInput: PlanInput): Int {
        return transactionManager.runBlock(
            block = {
                it.plansRepository.createPlan(monitorID = monitorID, plan = planInput)
            }
        )
    }

    override fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int): Pair<String, LocalDate> {
      return transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.isMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient

                val plan = it.plansRepository.getPlan(planID = planID) ?: throw PlanNotFound

                val endDate = startDate.plusDays((plan.dailyLists.size - 1).toLong())

                if (it.plansRepository.checkIfExistsPlanOfClientInThisPeriod(clientID = clientID, startDate = startDate, endDate = endDate)) throw ClientAlreadyHavePlanInThisPeriod

                it.plansRepository.associatePlanToClient(planID = planID, clientID = clientID, startDate = startDate, endDate = endDate)

                Pair(first = plan.title, second = startDate)
            }
        )
    }

    override fun getPlan(monitorID: UUID, planID: Int): PlanOutput {
        return transactionManager.runBlock(
            block = {
                if (!it.plansRepository.checkIfPlanIsOfMonitor(monitorID = monitorID, planID = planID)) throw NotPlanOfMonitor
                it.plansRepository.getPlanOfMonitor(planID) ?: throw PlanNotFound
            }
        )
    }

    override fun getPlans(monitorID: UUID): List<PlanInfoOutput> {
        return transactionManager.runBlock(
            block = {
                it.plansRepository.getPlans(monitorID = monitorID)
            }
        )
    }

    override fun giveFeedbackOfExercise(
        monitorID: UUID,
        planID: Int,
        dailyListID: Int,
        dailyExerciseID: Int,
        set: Int,
        feedback: String,
        clientID: UUID
    ) {
        return transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.isMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient

                if (!it.plansRepository.checkIfClientAlreadyUploadedVideo(
                        clientID = clientID,
                        planID = planID,
                        dailyListID = dailyListID,
                        exerciseID = dailyExerciseID,
                        set = set
                    )
                ) {
                    throw HasNotUploadedVideo
                }

                if (!it.plansRepository.checkIfMonitorHasPrescribedExercise(
                        planID = planID,
                        exerciseID = dailyExerciseID,
                        monitorID = monitorID
                    )
                ) {
                    throw NotPlanOfMonitor
                }

                it.plansRepository.giveFeedBackOfVideo(
                    clientID = clientID,
                    exerciseID = dailyExerciseID,
                    set = set,
                    feedBack = feedback
                )
            }
        )
    }

    override fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientDailyExercises> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.exercisesOfClients(monitorID = monitorID, date = date)
            }
        )
}
