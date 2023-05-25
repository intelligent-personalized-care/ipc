package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.Client
import pt.ipc.domain.Exercise
import pt.ipc.domain.RequestNotExists
import pt.ipc.domain.Role
import pt.ipc.domain.Unauthorized
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.toLocalDate
import pt.ipc.http.models.RequestInformation
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import pt.ipc.storage.transaction.TransactionManager
import java.time.LocalDate
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

    override fun decideRequest(requestID: UUID, clientID: UUID, accept: Boolean) {
        transactionManager.runBlock({
            val requestInfo = it.clientsRepository.getRequestInformations(requestID = requestID) ?: throw RequestNotExists

            if (requestInfo.clientID != clientID) throw Unauthorized

            it.clientsRepository.decideRequest(requestID = requestID, clientID = clientID, monitorID = requestInfo.monitorID, accept = accept)
        })
    }

    override fun getRequestsOfClient(clientID: UUID): List<RequestInformation> =
        transactionManager.runBlock(
            block = {
                it.clientsRepository.getClientRequests(clientID = clientID)
            }
        )

    override fun getExercisesOfClient(clientID: UUID, date: LocalDate?): List<Exercise> {
        return transactionManager.runBlock(
            block = {
                if (date == null) {
                    it.exerciseRepository.getAllExercisesOfClient(clientID = clientID)
                } else {
                    it.exerciseRepository.getExercisesOfDay(clientID = clientID, date = date)
                }
            }
        )
    }

    override fun rateMonitor(monitorID : UUID, clientID: UUID, rating : Int){
        transactionManager.runBlock(
            block = {
                if(!it.monitorRepository.checkIfIsMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient
                if(it.clientsRepository.hasClientRatedMonitor(clientID = clientID, monitorID = monitorID)) throw AlreadyRatedThisMonitor
                it.clientsRepository.rateMonitor(clientID = clientID, monitorID = monitorID, rating = rating)
            }
        )
    }

    override fun uploadVideoOfClient(video : ByteArray, clientID : UUID, planID : Int, dailyListID : Int, exerciseID : Int){
        val exerciseVideoID = UUID.randomUUID()
        transactionManager.runBlock(
            block = {
                if(it.clientsRepository.checkIfClientHasThisExercise(clientID = clientID, planID = planID, dailyList = dailyListID, exerciseID = exerciseID)) throw ClientDontHaveThisExercise
                if(it.clientsRepository.checkIfClientAlreadyUploadedVideo(clientID = clientID,exerciseID = exerciseID)) throw ExerciseAlreadyUploaded
                it.clientsRepository.uploadExerciseVideoOfClient(
                    clientID = clientID,
                    exerciseID = exerciseID,
                    exerciseVideoID = exerciseVideoID,
                    date = LocalDate.now()
                )
                it.cloudStorage.uploadClientVideo(fileName = exerciseVideoID,video)
                    },
            fileName = exerciseVideoID
        )
    }
}
