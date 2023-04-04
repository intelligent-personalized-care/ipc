package pt.ipc.domain

import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email : String,
    val password: String,
    val weigth : Int,
    val heigth : Int,
    val birthDate : LocalDate
){
    companion object{
        fun isPasswordSafe(password: String) : Boolean{
            val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$")
            return regex.matches(password)
        }
    }


}


