package pt.ipc.services.users.dtos

data class RegisterClientInput(
    val email: String,
    val name: String,
    val password: String,
    val weight : Int,
    val height : Int,
    val birthDate : String,
    val physicalCondition : String
)