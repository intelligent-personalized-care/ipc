package pt.ipc_app.preferences

import pt.ipc_app.domain.user.Role
import java.util.UUID

data class UserInfo(
    val id: UUID,
    val name: String,
    val token: String,
    val role: Role,
)