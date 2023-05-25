package pt.ipc.services

import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.Plan
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.dtos.RegisterMonitorInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.*

interface MonitorService {

    fun registerMonitor(registerMonitorInput: RegisterMonitorInput): RegisterOutput

    fun getMonitor(monitorID: UUID): MonitorDetails

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails>

    fun updateProfilePicture(monitorID: UUID, photo: ByteArray)

    fun requestClient(monitorID: UUID, clientID: UUID): UUID

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun createPlan(monitorID: UUID, clientID: UUID, plan: Plan): Int

    fun getPlan(monitorID: UUID, planID: Int): PlanOutput
}
