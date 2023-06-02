package pt.ipc.http.models

import java.util.*

data class MonitorOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val stars: Float
)