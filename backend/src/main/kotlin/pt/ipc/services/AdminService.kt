package pt.ipc.services

import pt.ipc.services.dtos.MonitorInfo
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.UUID

interface AdminService {

    fun createAdminAccount(registerInput : RegisterInput) : RegisterOutput

    fun getUnverifiedMonitors() : List<MonitorInfo>

    fun getCredentialOfMonitor(monitorID : UUID) : ByteArray

    fun decideMonitorCredential(monitorID: UUID, accept : Boolean)

}