package pt.ipc_app.preferences

import pt.ipc_app.domain.user.Role

data class UserInfo(
    val name: String,
    val token: String,
    val role: Role,
)