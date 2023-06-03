package pt.ipc_app.domain

import java.time.LocalDate

fun String.toLocalDate(): LocalDate {
    val splitDate = this.split("-")
    val year = splitDate[0].toInt()
    val month = splitDate[1].toInt()
    val day = splitDate[2].toInt()
    return LocalDate.of(year, month, day)
}