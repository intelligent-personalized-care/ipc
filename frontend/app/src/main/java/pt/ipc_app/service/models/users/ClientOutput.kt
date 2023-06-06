package pt.ipc_app.service.models.users

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ClientOutput(
    val id: UUID,
    val name: String,
    val email: String,
): Parcelable