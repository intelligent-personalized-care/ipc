package pt.ipc.domain.client

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Client(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String,
    val weight: Int? = null,
    val height: Int? = null,
    val physicalCondition: String? = null,
    val birthDate: LocalDate? = null
)

fun String.toLocalDate(): LocalDate = LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)

data class ClientOutput(
    val id: UUID,
    val name: String,
    val email: String,
    val weight: Int? = null,
    val height: Int? = null,
    val physicalCondition: String? = null,
    val birthDate: LocalDate? = null
)
