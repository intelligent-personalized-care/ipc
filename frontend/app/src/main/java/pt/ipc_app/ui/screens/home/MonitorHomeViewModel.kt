package pt.ipc_app.ui.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [MonitorHomeActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorHomeViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _clients = MutableStateFlow<ClientsOfMonitor?>(null)
    val clients
        get() = _clients.asStateFlow()

    private val _requests = MutableStateFlow<RequestsOfMonitor?>(null)
    val requests
        get() = _requests.asStateFlow()

    fun getClientsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getClientsOfMonitor(sessionManager.userUUID, sessionManager.userLoggedIn.token)
            },
            onSuccess = {
                _clients.value = it
            }
        )
    }

    fun getRequestsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorRequests(sessionManager.userUUID, sessionManager.userLoggedIn.token)
            },
            onSuccess = {
                _requests.value = it
            }
        )
    }

    fun decideConnectionRequestOfClient(
        requestId: UUID,
        requestDecision: ConnectionRequestDecisionInput
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.decideConnectionRequest(
                    monitorId = sessionManager.userUUID,
                    requestId = requestId,
                    requestDecision = requestDecision,
                    token = sessionManager.userLoggedIn.token
                )
            },
            onSuccess = {

            }
        )
    }

}