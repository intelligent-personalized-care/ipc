package pt.ipc.storage.repositories

import pt.ipc.domain.ClientExercises
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.User
import pt.ipc.http.models.ClientOutput
import pt.ipc.http.models.Rating
import pt.ipc.http.models.RequestInformation
import java.time.LocalDate
import java.util.*

interface MonitorRepository {

    fun registerMonitor(user: User, encryptedToken: String)

    fun insertCredential(monitorID: UUID, dtSubmit : LocalDate)

    fun getUserByID(id: UUID) : User?

    fun getMonitor(monitorID: UUID): MonitorDetails?

    fun getClientOfMonitor(monitorID: UUID): List<ClientOutput>

    fun getMonitorOfClient(clientId: UUID): MonitorDetails?

    fun getMonitorRating(monitorID: UUID): Rating

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails>

    fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, accept: Boolean)

    fun getRequestInformation(requestID: UUID): RequestInformation?

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun checkIfMonitorIsVerified(monitorID: UUID): Boolean

    fun isMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean

    fun exercisesOfClients(monitorID: UUID, date : LocalDate) : List<ClientExercises>
}
