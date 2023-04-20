package pt.ipc.services.users

import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput
import java.util.*

interface ClientsService {

    fun registerClient(input: RegisterClientInput): RegisterOutput

    fun addProfilePicture(clientID: UUID, profilePicture : ByteArray)

}