package pt.ipc.services.users.dtos

import java.time.LocalDate

data class RegisterClientInput(
    val email: String,
    val name: String,
    val password: String,
    val weight : Int,
    val height : Int,
    val birthDate : LocalDate,
    val physicalCondition : String
)