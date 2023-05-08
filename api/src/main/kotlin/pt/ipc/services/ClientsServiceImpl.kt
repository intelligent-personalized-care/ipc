package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.Client
import pt.ipc.domain.RequestDecision
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.RequestNotExists
import pt.ipc.domain.Role
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.toLocalDate
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import pt.ipc.storage.transaction.TransactionManager
import java.util.*

@Service
class ClientsServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val usersServiceUtils: UsersServiceUtils
) : ClientsService {

    override fun registerClient(input: RegisterClientInput): RegisterOutput {
        usersServiceUtils.checkDetails(email = input.email, password = input.password)

        val (token, id) = usersServiceUtils.createCredentials(email = input.email, role = Role.CLIENT)

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

    override fun addProfilePicture(clientID: UUID, profilePicture: ByteArray) {
        val pictureID = UUID.randomUUID()
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = pictureID, file = profilePicture)
                it.clientsRepository.updateProfilePictureID(userID = clientID, pictureID)
            },
            fileName = pictureID
        )
    }

    override fun decideRequest(requestID: UUID, clientID: UUID, decision: RequestDecision) {
        transactionManager.runBlock(
            block = {
                val requestInformation = it.clientsRepository.getRequestInformations(requestID = requestID) ?: throw RequestNotExists
                val monitorID = requestInformation.monitorID
                if (requestInformation.clientID != clientID) throw Unauthorized
                it.clientsRepository.decideRequest(requestID = requestID, clientID = clientID, monitorID = monitorID, decision = decision)
            }
        )
    }

    override fun getRequestsOfclient(clientID: UUID): List<RequestInformation> =
        transactionManager.runBlock(
            block = {
                it.clientsRepository.getClientRequests(clientID = clientID)
            }
        )
}
