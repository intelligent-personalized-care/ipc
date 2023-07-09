package pt.ipc_app.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Plan(
    val id: Int,
    val title: String,
    val startDate: String,
    val dailyLists: List<DailyList>
) : Parcelable {

    fun days() =
        List(dailyLists.size) { idx ->
            startDate.toLocalDate().plusDays(idx.toLong())
        }


    fun getListOfDayIfExists(day: LocalDate): DailyList? {
        for (idx in dailyLists.indices) {
            if (startDate.toLocalDate().plusDays(idx.toLong()) == day)
                return dailyLists[idx]
        }
        return null
    }
}