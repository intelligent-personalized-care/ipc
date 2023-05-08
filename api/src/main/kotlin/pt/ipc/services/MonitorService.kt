package pt.ipc.services

import pt.ipc.domain.RequestInformation
import pt.ipc.services.dtos.RegisterMonitorInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.*

interface MonitorService {

    fun registerMonitor(registerMonitorInput: RegisterMonitorInput): RegisterOutput

    fun updateProfilePicture(monitorID: UUID, photo: ByteArray)

    fun requestClient(monitorID: UUID, clientID: UUID): UUID

    fun monitorRequests(monitorID: UUID): List<RequestInformation>
}
