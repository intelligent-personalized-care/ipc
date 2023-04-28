package pt.ipc.services.users.dtos

data class RegisterClientInput(
    val name: String,
    val email: String,
    val password: String,
    val weight: Int? = null,
    val height: Int? = null,
    val birthDate: String? = null,
    val physicalCondition: String? = null
)
