package pt.ipc_app.ui.screens.details

import android.content.Context
import coil.request.ImageRequest
import pt.ipc_app.service.UsersService
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [MonitorDetailsActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorDetailsViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    fun getProfilePicture(
        context: Context,
        monitorId: UUID
    ): ImageRequest =
        usersService.getProfilePicture(
            context = context,
            userId = monitorId,
            token = sessionManager.userLoggedIn.accessToken
        )

    /**
     * Attempts to connect the monitor with a client.
     */
    fun connectWithMonitor(
        monitorId: UUID,
        comment: String,
        onSuccess: () -> Unit = { }
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.connectMonitor(
                    monitorId = monitorId,
                    clientId = sessionManager.userUUID,
                    comment = comment.ifEmpty { null },
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = { onSuccess() }
        )
    }

    /**
     * Attempts to disconnects a client from his monitor.
     */
    fun disconnectMonitor(
        onSuccess: () -> Unit = { }
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.disconnectMonitor(
                    clientId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = { onSuccess() }
        )
    }

    /**
     * Attempts to rate a monitor.
     */
    fun rateMonitor(
        monitorId: UUID,
        stars: Int,
        onSuccess: () -> Unit = { }
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.rateMonitor(
                    monitorId = monitorId,
                    clientId = sessionManager.userUUID,
                    stars = stars,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = { onSuccess() }
        )
    }
}