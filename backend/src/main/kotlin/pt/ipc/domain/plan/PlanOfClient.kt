package pt.ipc.domain.plan

import java.time.LocalDate

data class PlanOfClient(
    val id: Int,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
