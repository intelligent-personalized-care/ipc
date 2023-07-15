package pt.ipc.services.clientService

import org.springframework.stereotype.Service
import pt.ipc.domain.Role
import pt.ipc.domain.client.Client
import pt.ipc.domain.client.ClientOutput
import pt.ipc.domain.client.toLocalDate
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.AlreadyRatedThisMonitor
import pt.ipc.domain.exceptions.ClientAlreadyHaveMonitor
import pt.ipc.domain.exceptions.ClientDontHaveThisExercise
import pt.ipc.domain.exceptions.ExerciseAlreadyUploaded
import pt.ipc.domain.exceptions.MonitorNotFound
import pt.ipc.domain.exceptions.NotMonitorOfClient
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.domain.exercises.Exercise
import pt.ipc.domain.monitor.MonitorAvailable
import pt.ipc.http.controllers.clients.models.RegisterClientInput
import pt.ipc.http.models.emitter.PostedVideo
import pt.ipc.http.models.emitter.RequestMonitor
import pt.ipc.services.ServiceUtils
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorOutput
import pt.ipc.storage.transaction.TransactionManager
import java.time.LocalDate
import java.util.UUID

@Service
class ClientsServiceImpl(
    private val transactionManager: TransactionManager,
    private val encryptionUtils: EncryptionUtils,
    private val serviceUtils: ServiceUtils
) : ClientsService {

    override fun registerClient(input: RegisterClientInput): CredentialsOutput {
        serviceUtils.checkDetails(email = input.email, password = input.password)

        val (userID, accessToken, refreshToken, sessionID) = serviceUtils.createCredentials(role = Role.CLIENT)

        val encryptedSession = encryptionUtils.encrypt(plainText = sessionID.toString())

        val encryptedClient = Client(
            id = userID,
            name = input.name,
            email = input.email,
            password = encryptionUtils.encrypt(input.password),
            weight = input.weight,
            height = input.height,
            physicalCondition = input.physicalCondition,
            birthDate = input.birthDate?.toLocalDate()
        )

        transactionManager.runBlock(
            block = {
                it.clientsRepository.registerClient(
                    input = encryptedClient,
                    sessionID = encryptedSession
                )
            }
        )

        return CredentialsOutput(id = userID, accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun addProfilePicture(clientID: UUID, profilePicture: ByteArray) {
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = clientID, file = profilePicture)
            }
        )
    }

    override fun getClientProfile(clientID: UUID): ClientOutput =
        transactionManager.runBlock(
            block = {
                it.clientsRepository.getClient(clientID = clientID) ?: throw UserNotExists
            }
        )

    override fun deleteConnection(clientID: UUID) {
        transactionManager.runBlock(
            block = {
                val monitor = it.monitorRepository.getMonitorOfClient(clientID = clientID) ?: throw MonitorNotFound
                it.clientsRepository.deleteConnection(monitorID = monitor.id, clientID = clientID)
            }
        )
    }

    override fun searchMonitorsAvailable(clientID: UUID, name: String?, skip: Int, limit: Int): List<MonitorAvailable> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.searchMonitorsAvailable(name = name, skip = skip, limit = limit, clientID)
            }
        )

    override fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): RequestMonitor {
        val requestID = UUID.randomUUID()

        return transactionManager.runBlock(
            block = {
                val client = it.clientsRepository.getClient(clientID = clientID) ?: throw UserNotExists
                if (it.monitorRepository.getMonitorOfClient(clientID) != null) throw ClientAlreadyHaveMonitor
                it.clientsRepository.requestMonitor(
                    requestID = requestID,
                    monitorID = monitorID,
                    clientID = clientID,
                    requestText = requestText
                )

                RequestMonitor(requestID = requestID, name = client.name, requestText = requestText, clientID = clientID)
            }
        )
    }

    override fun getMonitorOfClient(clientID: UUID): MonitorOutput {
        return transactionManager.runBlock(
            block = {
                val details = it.monitorRepository.getMonitorOfClient(clientID) ?: throw MonitorNotFound
                val stars = it.monitorRepository.getMonitorRating(details.id)
                MonitorOutput(id = details.id, name = details.name, email = details.email, rating = stars)
            }
        )
    }

    override fun getExercisesOfClient(clientID: UUID, date: LocalDate?, skip: Int, limit: Int): List<Exercise> {
        return transactionManager.runBlock(
            block = {
                if (date == null) {
                    it.exerciseRepository.getAllExercisesOfClient(clientID = clientID, skip = skip, limit = limit)
                } else {
                    it.exerciseRepository.getExercisesOfDay(clientID = clientID, date = date)
                }
            }
        )
    }

    override fun rateMonitor(monitorID: UUID, clientID: UUID, rating: Int) {
        transactionManager.runBlock(
            block = {
                if (!it.monitorRepository.isMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient
                if (it.clientsRepository.hasClientRatedMonitor(clientID = clientID, monitorID = monitorID)) throw AlreadyRatedThisMonitor
                it.clientsRepository.rateMonitor(clientID = clientID, monitorID = monitorID, rating = rating)
            }
        )
    }

    override fun uploadVideoOfClient(
        video: ByteArray,
        clientID: UUID,
        planID: Int,
        dailyListID: Int,
        exerciseID: Int,
        set: Int,
        feedback: String?
    ): Pair<UUID, PostedVideo?> {
        val exerciseVideoID = UUID.randomUUID()
        return transactionManager.runBlock(
            block = {
                val monitor = it.monitorRepository.getMonitorOfClient(clientID = clientID) ?: throw MonitorNotFound
                val client = it.clientsRepository.getClient(clientID = clientID) ?: throw UserNotExists

                if (!it.clientsRepository.checkIfClientHasThisExercise(clientID = clientID, planID = planID, dailyList = dailyListID, exerciseID = exerciseID)) throw ClientDontHaveThisExercise
                if (it.clientsRepository.checkIfClientAlreadyUploadedVideo(clientID = clientID, exerciseID = exerciseID, set = set)) throw ExerciseAlreadyUploaded

                val hasDone =
                    it.clientsRepository.uploadExerciseVideoOfClient(
                                    clientID = clientID,
                                    exerciseID = exerciseID,
                                    exerciseVideoID = exerciseVideoID,
                                    date = LocalDate.now(),
                                    clientFeedback = feedback,
                                    set = set
                                )

                it.cloudStorage.uploadClientVideo(fileName = exerciseVideoID, video)

                Pair(first = monitor.id, second = if (hasDone) PostedVideo(clientID = clientID, name = client.name, exerciseID = exerciseID) else null)
            },
            fileName = exerciseVideoID
        )
    }
}
