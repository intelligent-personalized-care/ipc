package pt.ipc.storage.repositories

import pt.ipc.domain.Client
import pt.ipc.domain.ClientOutput
import java.time.LocalDate
import java.util.*

interface ClientsRepository {

    fun existsEmail(email: String): Boolean

    fun getClient(clientID: UUID): ClientOutput?

    fun registerClient(input: Client, sessionID: String)

    fun requestMonitor(requestID: UUID, monitorID: UUID, clientID: UUID, requestText: String? = null)

    fun hasClientRatedMonitor(clientID: UUID, monitorID: UUID): Boolean

    fun rateMonitor(clientID: UUID, monitorID: UUID, rating: Int)

    fun checkIfClientHasThisExercise(clientID: UUID, planID: Int, dailyList: Int, exerciseID: Int): Boolean

    fun checkIfClientAlreadyUploadedVideo(clientID: UUID, exerciseID: Int, set: Int): Boolean

    fun uploadExerciseVideoOfClient(clientID: UUID, exerciseID: Int, exerciseVideoID: UUID, date: LocalDate, set: Int, clientFeedback: String?)
}
