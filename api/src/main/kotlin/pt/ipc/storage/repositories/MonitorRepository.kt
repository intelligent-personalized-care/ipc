package pt.ipc.storage.repositories

import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.User
import pt.ipc.http.models.RequestInformation
import java.time.LocalDate
import java.util.*

interface MonitorRepository {

    fun registerMonitor(user: User, date: LocalDate, encryptedToken: String)

    fun getMonitor(monitorID: UUID): MonitorDetails?

    fun getMonitorOfClient(clientId: UUID): MonitorDetails?

    fun getMonitorRanking(monitorID: UUID): Float

    fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails>

    fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, accept: Boolean)

    fun getRequestInformation(requestID: UUID): RequestInformation?

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun checkIfMonitorIsVerified(monitorID: UUID): Boolean

    fun checkIfIsMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean
}