package pt.ipc.api.service.users.dtos

data class RegisterOutput(
    val name: String,
    val token: String
)