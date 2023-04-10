package pt.ipc.services.users

import org.springframework.stereotype.Service
import pt.ipc.database_storage.artificialTransaction.ArtificialTransactionManager
import pt.ipc.domain.Client
import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput
import pt.ipc.domain.encryption.EncryptionUtils
import java.util.*

@Service
class ClientsServiceImpl(
    private val artificialTransactionManager: ArtificialTransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val usersServiceUtils: UsersServiceUtils,
): ClientsService {

    override fun registerClient(input: RegisterClientInput): RegisterOutput {

        usersServiceUtils.checkDetails(email = input.email, password = input.password)

        val (token,id) = usersServiceUtils.createCredentials(email = input.email, name = input.name)

        val encryptedToken = encryptionUtils.encrypt(token)

        val encryptedClient = Client(
            id = id,
            name = input.name,
            email = input.email,
            password = encryptionUtils.encrypt(input.password),
            weigth = input.weigth,
            heigth = input.heigth,
            birthDate = input.birthDate
        )

        artificialTransactionManager.runBlock(
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