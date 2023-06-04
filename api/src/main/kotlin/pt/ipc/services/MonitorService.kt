package pt.ipc.services

import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.dtos.RegisterMonitorInput
import pt.ipc.services.dtos.RegisterOutput
import java.time.LocalDate
import java.util.*

interface MonitorService {

    fun registerMonitor(registerMonitorInput: RegisterMonitorInput): RegisterOutput

    fun insertCredential(monitorID : UUID, credential : ByteArray)

    fun getMonitor(monitorID: UUID): MonitorDetails

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails>

    fun updateProfilePicture(monitorID: UUID, photo: ByteArray)

    fun getProfilePicture(monitorID: UUID) : ByteArray

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun decideRequest(requestID: UUID, monitorID: UUID, accept: Boolean)

    fun createPlan(monitorID: UUID, planInput: PlanInput) : Int

    fun associatePlanToClient(monitorID: UUID, clientID: UUID, startDate : LocalDate, planID: Int)

    fun getPlan(monitorID: UUID, planID: Int): PlanOutput

    fun giveFeedbackOfExercise(monitorID: UUID, exerciseID : Int, feedback : String)
}
