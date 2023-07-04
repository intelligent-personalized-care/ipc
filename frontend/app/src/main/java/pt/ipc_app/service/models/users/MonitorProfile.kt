package pt.ipc_app.service.models.users

import java.util.*

data class MonitorProfile(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating,
    val docState: String? = null
) {
    fun documentState(): DocState =
        DocState.values().first { it.name.lowercase() == docState }
}

enum class DocState {
    INVALID,
    WAITING,
    VALID
}
