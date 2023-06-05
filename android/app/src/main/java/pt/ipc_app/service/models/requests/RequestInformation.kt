package pt.ipc_app.service.models.requests

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class RequestsOfMonitor(
    val requests: List<RequestInformation>
): Parcelable

@Parcelize
data class RequestInformation(
    val requestID: UUID,
    val requestText: String? = null,
    val clientID: UUID,
    val clientName: String,
    val clientEmail: String
): Parcelable
