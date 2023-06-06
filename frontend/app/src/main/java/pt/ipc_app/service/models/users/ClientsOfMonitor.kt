package pt.ipc_app.service.models.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientsOfMonitor(
    val clients: List<ClientOutput>
): Parcelable