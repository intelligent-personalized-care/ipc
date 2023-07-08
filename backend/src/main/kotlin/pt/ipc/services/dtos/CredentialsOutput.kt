package pt.ipc.services.dtos

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.UUID

data class CredentialsOutput(
    val id: UUID,
    @ColumnName("token_hash") val token: String
)
