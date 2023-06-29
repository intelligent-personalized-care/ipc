package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.*
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.ClientAlreadyHavePlanInThisPeriod
import pt.ipc.domain.exceptions.HasNotUploadedVideo
import pt.ipc.domain.exceptions.MonitorNotFound
import pt.ipc.domain.exceptions.NotMonitorOfClient
import pt.ipc.domain.exceptions.NotPlanOfMonitor
import pt.ipc.domain.exceptions.PlanNotFound
import pt.ipc.domain.exceptions.RequestNotExists
import pt.ipc.http.models.ClientOutput
import pt.ipc.http.models.PlansOutput
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.MonitorService
import pt.ipc.services.dtos.RegisterInput
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

    override fun registerMonitor(registerInput: RegisterInput): RegisterOutput {

        usersServiceUtils.checkDetails(email = registerInput.email, password = registerInput.password)

        val (token, userID) = usersServiceUtils.createCredentials(role = Role.MONITOR)

        val encryptedToken = encryptionUtils.encrypt(token)

        val user = User(
            id = userID,
            name = registerInput.name,
            email = registerInput.email,
            passwordHash = encryptionUtils.encrypt(registerInput.password)
        )

        transactionManager.runBlock(
            block = {
                it.monitorRepository.registerMonitor(user = user, encryptedToken = encryptedToken)
            }
        )

        return RegisterOutput(id = userID, token = token)
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

    override fun getClientsOfMonitor(monitorID: UUID): List<ClientOutput> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getClientOfMonitor(monitorID = monitorID)
            }
        )

    override fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.searchMonitorsAvailable(name = name, skip = skip, limit = limit)
            }
        )

    override fun updateProfilePicture(monitorID: UUID, photo: ByteArray) {
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = monitorID, file = photo)
            }
        )
    }

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

    override fun decideRequest(requestID: UUID, monitorID: UUID, accept: Boolean) {
        transactionManager.runBlock(
            block = {
                val requestInformation = it.monitorRepository.getRequestInformation(requestID = requestID) ?: throw RequestNotExists

                it.monitorRepository.decideRequest(
                    requestID = requestID,
                    clientID = requestInformation.clientID,
                    monitorID = monitorID,
                    accept = accept
                )
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

    override fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int) {
        return transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.isMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient

                val plan = it.plansRepository.getPlan(planID = planID) ?: throw PlanNotFound

                val endDate = startDate.plusDays((plan.dailyLists.size - 1).toLong())

                if (it.plansRepository.checkIfExistsPlanOfClientInThisPeriod(clientID = clientID, startDate = startDate, endDate = endDate)) throw ClientAlreadyHavePlanInThisPeriod

                it.plansRepository.associatePlanToClient(planID = planID, clientID = clientID, startDate = startDate, endDate = endDate)
            }
        )
    }

    override fun getPlan(monitorID: UUID, planID: Int): PlanOutput {
        return transactionManager.runBlock(
            block = {
                if (!it.plansRepository.checkIfPlanIsOfMonitor(monitorID = monitorID, planID = planID)) throw NotPlanOfMonitor
                it.plansRepository.getPlan(planID) ?: throw PlanNotFound
            }
        )
    }

    override fun getPlans(monitorID: UUID): List<PlansOutput> {
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
                    )) throw HasNotUploadedVideo

                if (!it.plansRepository.checkIfMonitorHasPrescribedExercise(
                        planID = planID,
                        exerciseID = dailyExerciseID,
                        monitorID = monitorID
                    )) throw NotPlanOfMonitor

                it.plansRepository.giveFeedBackOfVideo(
                    clientID = clientID,
                    exerciseID = dailyExerciseID,
                    set = set,
                    feedBack = feedback
                )
            }
        )
    }

    override fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientExercises> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.exercisesOfClients(monitorID = monitorID, date = date)
            }
        )
}
