package pt.ipc.services

import pt.ipc.domain.Exercise
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.MonitorOutput
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import java.time.LocalDate
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): RegisterOutput

    fun addProfilePicture(clientID: UUID, profilePicture: ByteArray)

    fun loggin(email: String, password: String): RegisterOutput

    fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): UUID

    fun getMonitorOfClient(clientID: UUID): MonitorOutput

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput

    fun getExercisesOfClient(clientID: UUID, date: LocalDate?, skip: Int, limit: Int): List<Exercise>

    fun rateMonitor(monitorID: UUID, clientID: UUID, rating: Int)

    fun uploadVideoOfClient(video: ByteArray, clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int, clientFeedback: String?)
}
