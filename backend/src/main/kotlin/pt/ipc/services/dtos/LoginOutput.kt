package pt.ipc.services.dtos

import org.jdbi.v3.core.mapper.reflect.ColumnName
import pt.ipc.domain.Role
import java.util.UUID

data class LoginOutput(
    val id: UUID,
    @ColumnName("access_token_hash") val accessToken: String,
    @ColumnName("refresh_token_hash") val refreshToken: String,
    val name: String,
    val role: Role
)
