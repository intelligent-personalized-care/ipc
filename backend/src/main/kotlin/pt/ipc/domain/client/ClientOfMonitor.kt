package pt.ipc.domain.client

import pt.ipc.domain.plan.PlanOfClient
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
