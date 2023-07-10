package pt.ipc.services.clientService

import pt.ipc.domain.client.ClientOutput
import pt.ipc.domain.exercises.Exercise
import pt.ipc.domain.monitor.MonitorAvailable
import pt.ipc.http.controllers.clients.models.RegisterClientInput
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.MonitorOutput
import java.time.LocalDate
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): CredentialsOutput

    fun addProfilePicture(clientID: UUID, profilePicture: ByteArray)

    fun getClientProfile(clientID: UUID): ClientOutput

    fun searchMonitorsAvailable(clientID: UUID, name: String?, skip: Int, limit: Int): List<MonitorAvailable>

    fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): Pair<UUID, String>

    fun getMonitorOfClient(clientID: UUID): MonitorOutput

    fun getExercisesOfClient(clientID: UUID, date: LocalDate?, skip: Int, limit: Int): List<Exercise>

    fun rateMonitor(monitorID: UUID, clientID: UUID, rating: Int)

    fun uploadVideoOfClient(video: ByteArray, clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int, set: Int, feedback: String? = null): Pair<UUID, String>
}
