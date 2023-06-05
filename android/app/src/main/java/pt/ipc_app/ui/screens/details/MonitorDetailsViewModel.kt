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

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

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
                    clientId = UUID.fromString(sessionManager.userInfo!!.id),
                    token = sessionManager.userInfo!!.token
                )
            },
            onSuccess = { }
        )
    }
}