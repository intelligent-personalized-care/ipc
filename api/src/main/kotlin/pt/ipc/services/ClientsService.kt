package pt.ipc.services

import pt.ipc.domain.RequestDecision
import pt.ipc.domain.RequestInformation
import pt.ipc.services.dtos.RegisterClientInput
import pt.ipc.services.dtos.RegisterOutput
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): RegisterOutput

    fun addProfilePicture(clientID: UUID, profilePicture: ByteArray)

    fun decideRequest(requestID: UUID, clientID: UUID, decision: RequestDecision)

    fun getRequestsOfclient(clientID: UUID): List<RequestInformation>
}
