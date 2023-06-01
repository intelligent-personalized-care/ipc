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

    private fun String.getLocalDate(): LocalDate {
        val splitDate = this.split("-")
        val year = splitDate[0].toInt()
        val month = splitDate[1].toInt()
        val day = splitDate[2].toInt()
        return LocalDate.of(year, month, day)
    }

    fun getListOfDayIfExists(day: LocalDate): DailyList? {
        for (idx in dailyLists.indices) {
            if (startDate.getLocalDate().plusDays(idx.toLong()) == day)
                return dailyLists[idx]
        }
        return null
    }
}