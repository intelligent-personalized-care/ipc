package pt.ipc.services

import pt.ipc.domain.Exercise
import pt.ipc.domain.PlanOutput
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import java.time.LocalDate
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): RegisterOutput

    fun addProfilePicture(clientID: UUID, profilePicture: ByteArray)

    fun requestMonitor(monitorID: UUID, clientID: UUID, requestText: String?): UUID

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput

    fun getExercisesOfClient(clientID: UUID, date: LocalDate?): List<Exercise>

    fun rateMonitor(monitorID: UUID, clientID: UUID, rating: Int)

    fun uploadVideoOfClient(video: ByteArray, clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int)
}
