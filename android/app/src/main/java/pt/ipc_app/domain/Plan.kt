package pt.ipc_app.domain

import pt.ipc_app.domain.DailyList
import java.time.LocalDate

data class Plan(
    val id: Int,
    val title: String,
    val dailyLists: List<DailyList>
) {
    val duration = dailyLists.size

    fun getListOfTodayIfExists(): DailyList? = dailyLists.firstOrNull { it.day == LocalDate.now() }
}