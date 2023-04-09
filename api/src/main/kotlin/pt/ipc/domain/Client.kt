package pt.ipc.domain

import java.time.LocalDate
import java.util.UUID

data class Client(
    val id: UUID,
    val name: String,
    val email : String,
    val password: String,
    val weigth : Int,
    val heigth : Int,
    val birthDate : LocalDate
)


