package pt.ipc.service.sections.users.dtos

data class RegisterInput(
    val name: String,
    val password: String,
    val email: String,
)