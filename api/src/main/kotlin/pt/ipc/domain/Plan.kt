package pt.ipc.domain

import java.time.LocalDate


data class Plan(
    val title : String,
    val startDate : LocalDate,
    val dailyLists: List<DailyList?>
)

data class PlanOutput(
    val planID : Int,
    val plan : Plan
)
