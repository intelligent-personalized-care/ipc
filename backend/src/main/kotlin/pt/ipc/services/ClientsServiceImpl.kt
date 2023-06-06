package pt.ipc.services

import org.springframework.stereotype.Service
import pt.ipc.domain.Client
import pt.ipc.domain.Exercise
import pt.ipc.domain.PlanOutput
import pt.ipc.domain.Role
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.exceptions.AlreadyRatedThisMonitor
import pt.ipc.domain.exceptions.ClientAlreadyHaveMonitor
import pt.ipc.domain.exceptions.ClientDontHavePlan
import pt.ipc.domain.exceptions.ClientDontHaveThisExercise
import pt.ipc.domain.exceptions.ExerciseAlreadyUploaded
import pt.ipc.domain.exceptions.MonitorNotFound
import pt.ipc.domain.exceptions.NotMonitorOfClient
import pt.ipc.domain.toLocalDate
import pt.ipc.http.models.MonitorOutput
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
        transactionManager.runBlock(
            block = {
                it.cloudStorage.uploadProfilePicture(fileName = clientID, file = profilePicture)
            }
        )
    }

    override fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): UUID {
        val requestID = UUID.randomUUID()

        transactionManager.runBlock(
            block = {
                if (it.monitorRepository.getMonitorOfClient(clientID) != null) throw ClientAlreadyHaveMonitor

                it.clientsRepository.requestMonitor(
                    requestID = requestID,
                    monitorID = monitorID,
                    clientID = clientID,
                    requestText = requestText
                )
            }
        )
        return requestID
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
                if (!it.monitorRepository.checkIfIsMonitorOfClient(monitorID = monitorID, clientID = clientID)) throw NotMonitorOfClient
                if (it.clientsRepository.hasClientRatedMonitor(clientID = clientID, monitorID = monitorID)) throw AlreadyRatedThisMonitor
                it.clientsRepository.rateMonitor(clientID = clientID, monitorID = monitorID, rating = rating)
            }
        )
    }

    override fun uploadVideoOfClient(video: ByteArray, clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int, clientFeedback: String?) {
        val exerciseVideoID = UUID.randomUUID()
        transactionManager.runBlock(
            block = {
                if (!it.clientsRepository.checkIfClientHasThisExercise(clientID = clientID, planID = planID, dailyList = dailyListID, exerciseID = exerciseID)) throw ClientDontHaveThisExercise
                if (it.clientsRepository.checkIfClientAlreadyUploadedVideo(clientID = clientID, exerciseID = exerciseID)) throw ExerciseAlreadyUploaded
                it.clientsRepository.uploadExerciseVideoOfClient(
                    clientID = clientID,
                    exerciseID = exerciseID,
                    exerciseVideoID = exerciseVideoID,
                    date = LocalDate.now(),
                    clientFeedback = clientFeedback
                )
                it.cloudStorage.uploadClientVideo(fileName = exerciseVideoID, video)
            },
            fileName = exerciseVideoID
        )
    }
}
