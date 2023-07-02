package pt.ipc.services.serviceImpl

import org.springframework.stereotype.Service
import pt.ipc.domain.Client
import pt.ipc.domain.ClientOutput
import pt.ipc.domain.Exercise
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.Role
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.AlreadyRatedThisMonitor
import pt.ipc.domain.exceptions.ClientAlreadyHaveMonitor
import pt.ipc.domain.exceptions.ClientDontHavePlan
import pt.ipc.domain.exceptions.ClientDontHaveThisExercise
import pt.ipc.domain.exceptions.ExerciseAlreadyUploaded
import pt.ipc.domain.exceptions.LoginFailed
import pt.ipc.domain.exceptions.MonitorNotFound
import pt.ipc.domain.exceptions.NotMonitorOfClient
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.domain.toLocalDate
import pt.ipc.http.models.MonitorAvailable
import pt.ipc.http.models.MonitorOutput
import pt.ipc.services.ClientsService
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.RegisterClientInput
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

        val (token, id) = serviceUtils.createCredentials(role = Role.CLIENT)

        val encryptedToken = encryptionUtils.encrypt(token)

        val encryptedClient = Client(
            id = id,
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
                    token = encryptedToken
                )
            }
        )

        return CredentialsOutput(id = id, token = token)
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

    override fun login(email: String, password: String): CredentialsOutput =
        transactionManager.runBlock(
            block = {
                val hashedPassword = encryptionUtils.encrypt(plainText = password)
                val credentials = it.clientsRepository.login(email = email, passwordHash = hashedPassword) ?: throw LoginFailed

                credentials.copy(token = encryptionUtils.decrypt(encryptedText = credentials.token))
            }
        )

    override fun searchMonitorsAvailable(clientID: UUID, name: String?, skip: Int, limit: Int): List<MonitorAvailable> =
        transactionManager.runBlock(
            block = {
                it.monitorRepository.searchMonitorsAvailable(name = name, skip = skip, limit = limit, clientID)
            }
        )

    override fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): Pair<UUID, String> {
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
                Pair(first = requestID, second = client.name)
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

    override fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput =
        transactionManager.runBlock(
            block = {
                it.plansRepository.getPlanOfClientContainingDate(clientID = clientID, date = date) ?: throw ClientDontHavePlan
            }
        )

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
    ): Pair<UUID, String> {
        val exerciseVideoID = UUID.randomUUID()
        return transactionManager.runBlock(
            block = {
                val monitor = it.monitorRepository.getMonitorOfClient(clientID = clientID) ?: throw MonitorNotFound
                val client = it.clientsRepository.getClient(clientID = clientID) ?: throw UserNotExists

                if (!it.clientsRepository.checkIfClientHasThisExercise(clientID = clientID, planID = planID, dailyList = dailyListID, exerciseID = exerciseID)) throw ClientDontHaveThisExercise
                if (it.clientsRepository.checkIfClientAlreadyUploadedVideo(clientID = clientID, exerciseID = exerciseID, set = set)) throw ExerciseAlreadyUploaded

                it.clientsRepository.uploadExerciseVideoOfClient(
                    clientID = clientID,
                    exerciseID = exerciseID,
                    exerciseVideoID = exerciseVideoID,
                    date = LocalDate.now(),
                    clientFeedback = feedback,
                    set = set
                )

                it.cloudStorage.uploadClientVideo(fileName = exerciseVideoID, video)

                Pair(first = monitor.id, second = client.name)
            },
            fileName = exerciseVideoID
        )
    }
}
