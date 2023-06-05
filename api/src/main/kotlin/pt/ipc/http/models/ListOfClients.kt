package pt.ipc.http.models

import java.util.*

class ListOfClients(val clients: List<ClientOutput>)

data class ClientOutput(
    val id: UUID,
    val name: String,
    val email: String
)
