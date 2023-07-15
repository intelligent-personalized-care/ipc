package pt.ipc.storage.repositories

import pt.ipc.domain.client.Client
import pt.ipc.domain.client.ClientOutput
import java.time.LocalDate
import java.util.*

interface ClientsRepository {

    fun existsEmail(email: String): Boolean

    fun getClient(clientID: UUID): ClientOutput?

    fun registerClient(input: Client, sessionID: String)

    fun requestMonitor(requestID: UUID, monitorID: UUID, clientID: UUID, requestText: String? = null)

    fun deleteConnection(monitorID: UUID, clientID: UUID)

    fun hasClientRatedMonitor(clientID: UUID, monitorID: UUID): Boolean

    fun rateMonitor(clientID: UUID, monitorID: UUID, rating: Int)

    fun checkIfClientHasThisExercise(clientID: UUID, planID: Int, dailyList: Int, exerciseID: Int): Boolean

    fun checkIfClientAlreadyUploadedVideo(clientID: UUID, exerciseID: Int, set: Int): Boolean

    fun uploadExerciseVideoOfClient(clientID: UUID, exerciseID: Int, exerciseVideoID: UUID, date: LocalDate, set: Int, clientFeedback: String?): Boolean

    fun getClientsVideosIDs(): List<UUID>

    fun deleteClientVideoID(videoID: UUID)
}
