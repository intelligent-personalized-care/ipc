package pt.ipc.services

import pt.ipc.domain.ClientOutput
import pt.ipc.domain.Exercise
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.MonitorAvailable
import pt.ipc.http.models.MonitorOutput
import pt.ipc.services.dtos.CredentialsOutput
import pt.ipc.services.dtos.RegisterClientInput
import java.time.LocalDate
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): CredentialsOutput

    fun addProfilePicture(clientID: UUID, profilePicture: ByteArray)

    fun getClientProfile(clientID: UUID): ClientOutput

    fun searchMonitorsAvailable(clientID: UUID, name: String?, skip: Int, limit: Int): List<MonitorAvailable>

    fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): Pair<UUID, String>

    fun getMonitorOfClient(clientID: UUID): MonitorOutput

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput

    fun getExercisesOfClient(clientID: UUID, date: LocalDate?, skip: Int, limit: Int): List<Exercise>

    fun rateMonitor(monitorID: UUID, clientID: UUID, rating: Int)

    fun uploadVideoOfClient(video: ByteArray, clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int, set: Int, feedback: String? = null): Pair<UUID, String>
}
