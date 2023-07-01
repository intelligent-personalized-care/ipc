package pt.ipc.http.models

import pt.ipc.domain.MonitorDetails
import java.util.*

data class AllMonitorsAvailableOutput(
    val monitors: List<MonitorAvailable>
)

data class MonitorAvailable(
    val id: UUID,
    val name: String,
    val email: String,
    val requested : Boolean
)
