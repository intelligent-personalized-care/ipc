package pt.ipc_app.ui.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestsOfMonitor
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

    private val _requests = MutableStateFlow<RequestsOfMonitor?>(null)
    val requests
        get() = _requests.asStateFlow()

    fun getRequestsOfMonitor() {
        val user = sessionManager.userInfo ?: throw IllegalArgumentException()

        launchAndExecuteRequest(
            request = {
                usersService.getMonitorRequests(UUID.fromString(user.id), user.token)
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
        val user = sessionManager.userInfo ?: throw IllegalArgumentException()

        launchAndExecuteRequest(
            request = {
                usersService.decideConnectionRequest(
                    monitorId = UUID.fromString(user.id),
                    requestId = requestId,
                    requestDecision = requestDecision,
                    token = user.token
                )
            },
            onSuccess = {}
        )
    }

}