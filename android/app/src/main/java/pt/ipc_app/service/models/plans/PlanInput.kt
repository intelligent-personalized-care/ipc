package pt.ipc_app.service.models.plans

import pt.ipc_app.domain.DailyList
import pt.ipc_app.service.models.dailyList.DailyListInput
import java.time.LocalDate

data class PlanInput(
    val title: String,
    val startDate: String,
    val dailyLists: MutableList<DailyListInput?> = mutableListOf()
) {
    fun addDailyList(dailyList: DailyListInput) = dailyLists.add(dailyList)
}