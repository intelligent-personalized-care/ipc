package pt.ipc.storage.repositories

import pt.ipc.domain.client.ClientDailyExercises
import pt.ipc.domain.client.ClientOfMonitor
import pt.ipc.domain.monitor.MonitorDetails
import pt.ipc.domain.User
import pt.ipc.domain.client.ClientInformation
import pt.ipc.domain.monitor.MonitorAvailable
import pt.ipc.domain.monitor.MonitorProfile
import pt.ipc.domain.monitor.Rating
import pt.ipc.domain.monitor.RequestInformation
import java.time.LocalDate
import java.util.UUID

interface MonitorRepository {

    fun registerMonitor(user: User, sessionID: String)

    fun insertCredential(monitorID: UUID, dtSubmit: LocalDate)

    fun getUserByIDAndSession(id: UUID, sessionID: String): User?

    fun getMonitorProfile(monitorID: UUID): MonitorProfile?

    fun getClientOfMonitor(monitorID: UUID, clientID: UUID): ClientOfMonitor?

    fun getMonitor(monitorID: UUID): MonitorDetails?

    fun getClientsOfMonitor(monitorID: UUID): List<ClientInformation>

    fun getMonitorOfClient(clientID: UUID): MonitorDetails?

    fun getMonitorRating(monitorID: UUID): Rating

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int, clientID: UUID): List<MonitorAvailable>

    fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, decision: Boolean)

    fun getRequestInformation(requestID: UUID): RequestInformation?

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun checkIfMonitorIsVerified(monitorID: UUID): Boolean

    fun isMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean

    fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientDailyExercises>
}
