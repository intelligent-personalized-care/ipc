package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Plan(
    val title: String,
    val startDate: LocalDate,
    val dailyLists: List<DailyList>
) : Parcelable {
    val duration = dailyLists.size

    fun getListOfDayIfExists(day: LocalDate): DailyList? {
        var index = 0L
        return dailyLists.firstOrNull { day.plusDays(index++) == day }
    }
}