package pt.ipc.domain

import java.time.LocalDate

data class PlanInput(
    val title: String,
    val dailyLists: List<DailyListInput?>
)

data class PlanOutput(
    val id: Int,
    val title: String,
    val startDate: LocalDate? = null,
    val dailyLists: List<DailyListOutput?>
)
