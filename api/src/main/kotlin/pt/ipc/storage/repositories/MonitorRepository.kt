package pt.ipc.storage.repositories

import pt.ipc.domain.User
import pt.ipc.http.models.RequestInformation
import java.time.LocalDate
import java.util.*

interface MonitorRepository {

    fun registerMonitor(user: User, date: LocalDate, encryptedToken: String)

    fun requestClient(requestID: UUID, monitorID: UUID, clientID: UUID)

    fun monitorRequests(monitorID: UUID): List<RequestInformation>

    fun checkIfMonitorIsVerified(monitorID: UUID): Boolean

    fun checkIfIsMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean
}
