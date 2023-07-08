package pt.ipc.services

import pt.ipc.domain.ClientDailyExercises
import pt.ipc.domain.ClientOfMonitor
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.ClientInformation
import pt.ipc.http.models.MonitorProfile
import pt.ipc.http.models.PlanInfoOutput
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.dtos.CredentialsOutput
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

    fun decideRequest(requestID: UUID, monitorID: UUID, accept: Boolean): Triple<List<ClientInformation>, UUID, String>

    fun createPlan(monitorID: UUID, planInput: PlanInput): Int

    fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int): String

    fun getPlan(monitorID: UUID, planID: Int): PlanOutput

    fun getPlans(monitorID: UUID): List<PlanInfoOutput>

    fun giveFeedbackOfExercise(monitorID: UUID, planID: Int, dailyListID: Int, dailyExerciseID: Int, set: Int, feedback: String, clientID: UUID)

    fun getPlanOfClient(clientID: UUID, planID: Int): PlanOutput

    fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientDailyExercises>
}
