package pt.ipc.api.service.users.dtos

data class RegisterInput(
    val name: String,
    val password: String,
    val email: String,
)