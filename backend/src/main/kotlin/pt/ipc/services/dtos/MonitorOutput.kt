package pt.ipc.services.dtos

import pt.ipc.domain.monitor.Rating
import java.util.*

data class MonitorOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val rating: Rating
)
