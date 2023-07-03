package pt.ipc_app.ui.screens.details

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
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

    fun getProfilePictureUrl(
        monitorId: UUID
    ): String =
        usersService.getProfilePictureUrl(monitorId)

    /**
     * Attempts to connect the monitor with a client.
     */
    fun connectWithMonitor(
        monitorId: UUID
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.connectMonitor(
                    monitorId = monitorId,
                    clientId = UUID.fromString(sessionManager.userLoggedIn.id),
                    token = sessionManager.userLoggedIn.token
                )
            },
            onSuccess = { }
        )
    }
}