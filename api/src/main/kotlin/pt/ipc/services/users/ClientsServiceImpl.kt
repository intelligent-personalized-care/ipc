package pt.ipc.services.users

import org.springframework.stereotype.Service
import pt.ipc.database_storage.artificialTransaction.TransactionManager
import pt.ipc.domain.Client
import pt.ipc.domain.Role
import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.toLocalDate
import java.util.*

@Service
class ClientsServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val usersServiceUtils: UsersServiceUtils,
): ClientsService {

    override fun registerClient(input: RegisterClientInput): RegisterOutput {

        usersServiceUtils.checkDetails(email = input.email, password = input.password)

        val (token,id) = usersServiceUtils.createCredentials(email = input.email, role = Role.CLIENT)

        val encryptedToken = encryptionUtils.encrypt(token)

        val encryptedClient = Client(
            id = id,
            name = input.name,
            email = input.email,
            password = encryptionUtils.encrypt(input.password),
            weight = input.weight,
            height = input.height,
            birthDate = input.birthDate?.toLocalDate()
        )

        transactionManager.runBlock(
          block = {
                it.clientsRepository.registerClient(
                    input = encryptedClient,
                    token = encryptedToken,
                    physicalCondition = input.physicalCondition
                )
            }
        )

        return RegisterOutput(id = id, token = token)

    }

    override fun addMonitorRequest(clientID : Int, monitorID : Int) : UUID{
        TODO()
    }

}