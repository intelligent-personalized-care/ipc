package pt.ipc.services

import pt.ipc.domain.ClientExercises
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.ClientOutput
import pt.ipc.http.models.PlansOutput
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.services.dtos.CredentialsOutput
import java.time.LocalDate
import java.util.*

interface MonitorService {

    fun registerMonitor(registerInput: RegisterInput): CredentialsOutput

    fun insertCredential(monitorID: UUID, credential: ByteArray)

    fun getMonitor(monitorID: UUID): MonitorDetails

    fun getClientsOfMonitor(monitorID: UUID): List<ClientOutput>

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails>

    fun updateProfilePicture(monitorID: UUID, photo: ByteArray)

    fun getProfilePicture(monitorID: UUID): ByteArray

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun decideRequest(requestID: UUID, monitorID: UUID, accept: Boolean) : List<ClientOutput>

    fun createPlan(monitorID: UUID, planInput: PlanInput): Int

    fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate: LocalDate, planID: Int)

    fun getPlan(monitorID: UUID, planID: Int): PlanOutput

    fun getPlans(monitorID: UUID): List<PlansOutput>

    fun giveFeedbackOfExercise(monitorID: UUID, planID : Int,dailyListID : Int,dailyExerciseID : Int,set : Int, feedback: String, clientID: UUID)

    fun exercisesOfClients(monitorID: UUID, date : LocalDate) : List<ClientExercises>
}
