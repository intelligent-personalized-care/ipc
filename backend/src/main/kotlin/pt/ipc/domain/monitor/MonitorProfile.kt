package pt.ipc.domain.monitor

import java.util.UUID

data class MonitorProfile(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating,
    val docState: String? = null
)