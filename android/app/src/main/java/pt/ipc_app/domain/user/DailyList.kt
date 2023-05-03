package pt.ipc_app.domain.user

import java.time.LocalDate

data class DailyList(
    val id: Int,
    val day: LocalDate,
    val exercises: List<Exercise>
)