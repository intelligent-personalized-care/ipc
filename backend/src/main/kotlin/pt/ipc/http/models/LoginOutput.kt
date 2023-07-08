package pt.ipc.http.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import pt.ipc.domain.Role
import java.util.UUID

data class LoginOutput(
    val id: UUID,
    @ColumnName("token_hash") val token: String,
    val name: String,
    val role: Role
)
