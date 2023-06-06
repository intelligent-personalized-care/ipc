package pt.ipc.http.models

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.UUID

data class RequestsOfMonitor(
    val requests: List<RequestInformation>
)

data class RequestInformation(
    val requestID: UUID,
    val requestText: String? = null,
    val clientID: UUID,
    @ColumnName("name")
    val clientName: String,
    @ColumnName("email")
    val clientEmail: String
)
