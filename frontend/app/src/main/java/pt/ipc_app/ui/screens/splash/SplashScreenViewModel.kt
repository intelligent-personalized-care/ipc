package pt.ipc_app.ui.screens.splash

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.sse.SseService
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [SplashScreenActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class SplashScreenViewModel(
    private val usersService: UsersService,
    private val sseService: SseService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _monitor = MutableStateFlow<MonitorOutput?>(null)
    val monitor
        get() = _monitor.asStateFlow()

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan
        get() = _plan.asStateFlow()

    private val _clients = MutableStateFlow<ClientsOfMonitor?>(null)
    val clients
        get() = _clients.asStateFlow()

    private val _requests = MutableStateFlow<RequestsOfMonitor?>(null)
    val requests
        get() = _requests.asStateFlow()

    fun subscribe() {
        sseService.start(sessionManager.userLoggedIn.accessToken)
    }

    fun unsubscribe() {
        launchAndExecuteRequest(
            request = {
                sseService.stop(sessionManager.userLoggedIn.accessToken)
            }
        )
    }

    fun getCurrentPlanOfClient() {
        launchAndExecuteRequest(
            request = {
                usersService.getCurrentPlanOfClient(clientId = sessionManager.userUUID, token = sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _plan.value = it
            }
        )
    }

    fun getMonitorOfClient() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorOfClient(sessionManager.userUUID, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _monitor.value = it
            }
        )
    }

    fun getClientsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getClientsOfMonitor(sessionManager.userUUID, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _clients.value = it
            }
        )
    }

    fun getRequestsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorRequests(sessionManager.userUUID, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _requests.value = it
            }
        )
    }

}