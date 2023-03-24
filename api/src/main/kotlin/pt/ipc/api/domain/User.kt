package pt.ipc.api.domain

data class User(
    val id: Int,
    val name: String,
    val password: PasswordValidationInfo
)

data class PasswordValidationInfo(
    val validationInfo: String
)