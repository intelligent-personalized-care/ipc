package pt.ipc.services.users

import pt.ipc.services.users.dtos.RegisterClientInput
import pt.ipc.services.users.dtos.RegisterOutput

interface ClientsService {

    fun registerClient(input: RegisterClientInput): RegisterOutput

}