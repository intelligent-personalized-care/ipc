package pt.ipc.services.monitorService

import pt.ipc.domain.client.ClientDailyExercises
import pt.ipc.domain.client.ClientInformation
import pt.ipc.domain.client.ClientOfMonitor
import pt.ipc.domain.monitor.MonitorDetails
import pt.ipc.domain.monitor.MonitorProfile
import pt.ipc.domain.monitor.RequestInformation
import pt.ipc.domain.plan.PlanInfoOutput
import pt.ipc.domain.plan.PlanInput
import pt.ipc.domain.plan.PlanOutput
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorOutput
import pt.ipc.services.dtos.RegisterInput
import java.time.LocalDate
import java.util.*

interface MonitorService {

    fun registerMonitor(registerInput: RegisterInput): CredentialsOutput

    fun insertCredential(monitorID: UUID, credential: ByteArray)

    fun getMonitor(monitorID: UUID): MonitorDetails

    fun getClientsOfMonitor(monitorID: UUID): List<ClientInformation>

    fun getClientOfMonitor(monitorID: UUID, clientID: UUID): ClientOfMonitor

    fun updateProfilePicture(monitorID: UUID, photo: ByteArray)

    fun getMonitorProfile(monitorID: UUID): MonitorProfile

    fun getProfilePicture(monitorID: UUID): ByteArray

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun decideRequest(requestID: UUID, monitorID: UUID): Triple<List<ClientInformation>, UUID, MonitorOutput>

    fun createPlan(monitorID: UUID, planInput: PlanInput): Int

    fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int): PlanOutput

    fun getPlan(monitorID: UUID, planID: Int): PlanOutput

    fun getPlans(monitorID: UUID): List<PlanInfoOutput>

    fun giveFeedbackOfExercise(monitorID: UUID, planID: Int, dailyListID: Int, dailyExerciseID: Int, set: Int, feedback: String, clientID: UUID)

    fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientDailyExercises>
}
