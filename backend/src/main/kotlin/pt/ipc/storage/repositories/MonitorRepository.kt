package pt.ipc.storage.repositories

import pt.ipc.domain.ClientExercises
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.User
import pt.ipc.http.models.ClientInformation
import pt.ipc.http.models.MonitorAvailable
import pt.ipc.http.models.MonitorProfile
import pt.ipc.http.models.Rating
import pt.ipc.http.models.RequestInformation
import java.time.LocalDate
import java.util.UUID

interface MonitorRepository {

    fun registerMonitor(user: User, encryptedToken: String)

    fun insertCredential(monitorID: UUID, dtSubmit: LocalDate)

    fun getUserByID(id: UUID): User?

    fun getMonitorProfile(monitorID: UUID): MonitorProfile?

    fun getMonitor(monitorID: UUID): MonitorDetails?

    fun getClientsOfMonitor(monitorID: UUID): List<ClientInformation>

    fun getMonitorOfClient(clientID: UUID): MonitorDetails?

    fun getMonitorRating(monitorID: UUID): Rating

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int, clientID: UUID): List<MonitorAvailable>

    fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, decision : Boolean)

    fun getRequestInformation(requestID: UUID): RequestInformation?

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun checkIfMonitorIsVerified(monitorID: UUID): Boolean

    fun isMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean

    fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientExercises>
}