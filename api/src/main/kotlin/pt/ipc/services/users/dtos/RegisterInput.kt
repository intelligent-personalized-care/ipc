package pt.ipc.services.users.dtos

import java.time.LocalDate

data class RegisterInput(
    val email: String,
    val name: String,
    val password: String,
    val weigth : Int,
    val heigth : Int,
    val birthDate : LocalDate,
    val physicalCondition : String
)