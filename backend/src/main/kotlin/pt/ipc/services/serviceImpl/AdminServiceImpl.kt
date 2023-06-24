package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.Role
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.services.AdminService
import pt.ipc.services.dtos.MonitorInfo
import pt.ipc.services.dtos.RegisterInput
import pt.ipc.services.dtos.RegisterOutput
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class AdminServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val usersServiceUtils: UsersServiceUtils
) : AdminService {

    override fun createAdminAccount(registerInput: RegisterInput): RegisterOutput {

        usersServiceUtils.checkDetails(email = registerInput.email, password = registerInput.password)

        val (token,id) = usersServiceUtils.createCredentials(role = Role.ADMIN)

        transactionManager.runBlock(
            block = {
                it.adminRepository.createAdmin(
                    id = id,
                    email = registerInput.email,
                    name = registerInput.name,
                    passwordHash = encryptionUtils.encrypt(plainText = registerInput.password),
                    tokenHash = encryptionUtils.encrypt(plainText = token)
                )
            }
        )

        return RegisterOutput(id = id, token = token)

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
    }
