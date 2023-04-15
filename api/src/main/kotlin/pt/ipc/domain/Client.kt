package pt.ipc.domain

import java.time.LocalDate
import java.util.UUID

data class Client(
    val id: UUID,
    val name: String,
    val email : String,
    val password: String,
    val weight : Int,
    val height : Int,
    val birthDate : LocalDate
)

fun String.toLocalDate(): LocalDate {
    val date = split("-")

    val year = date[0].toInt()
    val month = date[1].toInt()
    val day = date[2].toInt()

    return LocalDate.of(year, month, day)
}
