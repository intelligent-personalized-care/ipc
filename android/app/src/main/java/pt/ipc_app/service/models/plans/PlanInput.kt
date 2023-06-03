package pt.ipc_app.service.models.plans

import pt.ipc_app.service.models.dailyList.DailyListInput
import java.time.LocalDate

data class PlanInput(
    val title: String,
    val startDate: LocalDate,
    val dailyLists: List<DailyListInput?>
)