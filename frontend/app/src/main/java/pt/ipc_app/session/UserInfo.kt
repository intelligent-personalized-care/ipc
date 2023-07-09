package pt.ipc_app.session

import pt.ipc_app.domain.user.Role

data class UserInfo(
    val id: String,
    val name: String,
    val accessToken: String,
    val refreshToken: String,
    val role: Role,
)