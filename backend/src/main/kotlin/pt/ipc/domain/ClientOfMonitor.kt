package pt.ipc.domain

import java.time.LocalDate
import java.util.UUID

data class ClientOfMonitor(
    val id: UUID,
    val name: String,
    val email: String,
    val weight: Int? = null,
    val height: Int? = null,
    val physicalCondition: String? = null,
    val birthDate: LocalDate? = null,
    val plans: List<PlanOfClient>
)
