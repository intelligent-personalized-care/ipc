package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.Role
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.services.AdminService
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorInfo
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class AdminServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val serviceUtils: ServiceUtils
) : AdminService {

    override fun createAdminAccount(registerInput: RegisterInput): CredentialsOutput {
        serviceUtils.checkDetails(email = registerInput.email, password = registerInput.password)

        val (userID, accessToken, refreshToken, sessionID) = serviceUtils.createCredentials(role = Role.ADMIN)

        val encryptedSession = encryptionUtils.encrypt(plainText = sessionID.toString())

        transactionManager.runBlock(
            block = {
                it.adminRepository.createAdmin(
                    id = userID,
                    email = registerInput.email,
                    name = registerInput.name,
                    passwordHash = encryptionUtils.encrypt(plainText = registerInput.password),
                    sessionID = encryptedSession
                )
            }
        )

        return CredentialsOutput(id = userID, accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun getUnverifiedMonitors(): List<MonitorInfo> =
        transactionManager.runBlock(
            block = {
                it.adminRepository.getUnverifiedMonitors()
            }
        )

    override fun getCredentialOfMonitor(monitorID: UUID): ByteArray =
        transactionManager.runBlock(
            block = {
                it.cloudStorage.downloadMonitorCredentials(fileName = monitorID)
            }
        )

    override fun decideMonitorCredential(monitorID: UUID, accept: Boolean) =
        transactionManager.runBlock(
            block = {
                it.adminRepository.decideMonitorVerification(monitorID = monitorID, decision = accept)
            }
        )

    override fun addExerciseInfoPreview(title: String, description: String, type: ExerciseType, video: ByteArray) {
        val exerciseID = UUID.randomUUID()

        transactionManager.runBlock(
            block = {
                it.exerciseRepository.addExerciseInfoPreview(
                    exerciseID = exerciseID,
                    title = title,
                    description = description,
                    type = type
                )
                it.cloudStorage.uploadVideoPreview(fileName = exerciseID, file = video)
            }
        )
    }
}
