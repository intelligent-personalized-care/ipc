package pt.ipc_app.service.models.login

import pt.ipc_app.domain.user.Role
import java.util.*

data class LoginOutput(
    val id: UUID,
    val accessToken: String,
    val refreshToken: String,
    val name : String,
    val role : Role
)
