package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.*
import pt.ipc.http.models.ClientOutput
import pt.ipc.http.models.ListOfPlans
import pt.ipc.http.models.PlansOutput
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
                it.monitorRepository.registerMonitor(user = user, date = LocalDate.now(), encryptedToken = encryptedToken)
            }
        )

        return RegisterOutput(id = userID, token = token)
    }

    override fun insertCredential(monitorID : UUID, credential : ByteArray){
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadMonitorCredentials(fileName = monitorID, file = credential)
            }
        )
    }

    override fun getMonitor(monitorID: UUID): MonitorDetails =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getMonitor(monitorID = monitorID) ?: throw MonitorNotFound
            }
        )

    override fun getClientsOfMonitor(monitorID: UUID) : List<ClientOutput> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.getClientOfMonitor(monitorID = monitorID)
            }
        )

    override fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.searchMonitorsAvailable(name = name, skip =  skip, limit = limit)
            }
        )

    override fun updateProfilePicture(monitorID: UUID, photo: ByteArray) {
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = monitorID, file = photo)
            }
        )
    }

    override fun getProfilePicture(monitorID: UUID) : ByteArray{
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

                if (requestInformation.monitorID != monitorID) throw Unauthorized

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

    override fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int){
        return transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.checkIfIsMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient

                val plan = it.plansRepository.getPlan(planID = planID)

                val planEndDate = startDate.plusDays((plan.dailyLists.size - 1).toLong())

                if (it.plansRepository.checkIfExistsPlanOfClientInThisPeriod(clientID, startDate, planEndDate)) throw ClientAlreadyHavePlanInThisPeriod

                it.plansRepository.associatePlanToClient(planID = planID, clientID = clientID, startDate = startDate )
            }
        )
    }

    override fun getPlan(monitorID: UUID, planID: Int): PlanOutput {
        return transactionManager.runBlock(
            block = {
                if (!it.plansRepository.checkIfPlanIsOfMonitor(monitorID = monitorID, planID = planID)) throw NotPlanOfMonitor
                it.plansRepository.getPlan(planID)
            }
        )
    }

    override fun getPlans(monitorID: UUID) : List<PlansOutput>{
        return transactionManager.runBlock(
            block = {
                it.plansRepository.getPlans(monitorID = monitorID)
            }
        )
    }

    override fun giveFeedbackOfExercise(monitorID: UUID, exerciseID : Int, feedback : String){
        return transactionManager.runBlock(
            block = {
                if(!it.plansRepository.checkIfClientAlreadyUploadedVideo(exerciseID = exerciseID)) throw HasNotUploadedVideo
                if(!it.plansRepository.checkIfMonitorHasPrescribedExercise(exerciseID = exerciseID, monitorID = monitorID)) throw NotPlanOfMonitor
                it.plansRepository.giveFeedBackOfVideo(exerciseID = exerciseID, feedback = feedback)
            }
        )
    }
}
