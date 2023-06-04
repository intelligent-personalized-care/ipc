package pt.ipc_app.service.models.plans

import pt.ipc_app.service.models.dailyList.DailyListInput

data class PlanInput(
    val title: String,
    val dailyLists: MutableList<DailyListInput?> = mutableListOf()
) {
    fun addDailyList(dailyList: DailyListInput) = dailyLists.add(dailyList)
}