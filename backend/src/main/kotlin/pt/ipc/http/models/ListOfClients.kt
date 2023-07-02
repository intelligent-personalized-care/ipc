package pt.ipc.http.models

import java.util.*

class ListOfClients(val clients: List<ClientInformation>)

data class ClientInformation(
    val id: UUID,
    val name: String,
    val email: String
)
