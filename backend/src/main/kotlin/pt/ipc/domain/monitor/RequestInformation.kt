package pt.ipc.domain.monitor

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.UUID


data class RequestInformation(
    val requestID: UUID,
    val requestText: String? = null,
    @ColumnName("client_id") val clientID: UUID,
    @ColumnName("name") val clientName: String,
    @ColumnName("email") val clientEmail: String
)
