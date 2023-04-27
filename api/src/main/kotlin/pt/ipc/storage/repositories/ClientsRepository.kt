package pt.ipc.storage.repositories

import pt.ipc.domain.Client
import pt.ipc.domain.RequestDecision
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.Role
import pt.ipc.domain.User
import java.util.*

interface ClientsRepository {

    fun existsEmail(email: String): Boolean

    fun getUserByToken(token: String): Pair<User, Role>?

    fun registerClient(input: Client, token: String, physicalCondition: String? = null)

    fun updateProfilePictureID(userID: UUID, profileID: UUID)

    fun roleOfUser(id: UUID): Role

    fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, decision: RequestDecision)

    fun getRequestInformations(requestID: UUID): RequestInformation?

    fun getClientRequests(clientID: UUID): List<RequestInformation>
}
