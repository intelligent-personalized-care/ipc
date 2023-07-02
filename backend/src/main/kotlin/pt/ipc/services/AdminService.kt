package pt.ipc.services

import pt.ipc.domain.ExerciseType
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorInfo
import pt.ipc.services.dtos.RegisterInput
import java.util.UUID

interface AdminService {

    fun createAdminAccount(registerInput: RegisterInput): CredentialsOutput

    fun getUnverifiedMonitors(): List<MonitorInfo>

    fun getCredentialOfMonitor(monitorID: UUID): ByteArray

    fun decideMonitorCredential(monitorID: UUID, accept: Boolean)

    fun addExerciseInfoPreview(title: String, description: String, type: ExerciseType, video: ByteArray)
}
