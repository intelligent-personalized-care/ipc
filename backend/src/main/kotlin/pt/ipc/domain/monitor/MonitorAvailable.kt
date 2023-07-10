package pt.ipc.domain.monitor

import java.util.UUID

data class MonitorAvailable(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating? = null,
    val requested: Boolean
)
