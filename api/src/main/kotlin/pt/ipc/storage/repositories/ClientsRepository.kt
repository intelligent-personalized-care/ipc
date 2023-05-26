package pt.ipc.storage.repositories

import pt.ipc.domain.Client
import pt.ipc.domain.Role
import pt.ipc.domain.User
import java.time.LocalDate
import java.util.*

interface ClientsRepository {

    fun existsEmail(email: String): Boolean

    fun getUserByToken(token: String): Pair<User, Role>?

    fun registerClient(input: Client, token: String, physicalCondition: String? = null)

    fun updateProfilePictureID(userID: UUID, profileID: UUID)

    fun roleOfUser(id: UUID): Role

    fun requestMonitor(requestID: UUID, monitorID: UUID, clientID: UUID, requestText: String? = null )

    fun hasClientRatedMonitor(clientID: UUID, monitorID: UUID): Boolean

    fun rateMonitor(clientID: UUID, monitorID: UUID, rating: Int)

    fun checkIfClientHasThisExercise(clientID: UUID, planID: Int, dailyList: Int, exerciseID: Int): Boolean

    fun checkIfClientAlreadyUploadedVideo(clientID: UUID, exerciseID: Int): Boolean

    fun uploadExerciseVideoOfClient(clientID: UUID, exerciseID: Int, exerciseVideoID: UUID, date: LocalDate)
}
