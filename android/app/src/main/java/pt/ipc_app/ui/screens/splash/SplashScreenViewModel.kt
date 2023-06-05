package pt.ipc_app.ui.screens.splash

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [SplashScreenViewModel].
 *
 * @param sessionManager the manager used to handle the user session
 */
class SplashScreenViewModel(
    private val usersService: UsersService,
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

    fun getCurrentPlanOfClient() {
        val user = sessionManager.userInfo ?: throw IllegalArgumentException()

        launchAndExecuteRequest(
            request = {
                usersService.getCurrentPlanOfClient(UUID.fromString(user.id), user.token)
            },
            onSuccess = {
                _plan.value = it
            }
        )
    }

    fun getMonitorOfClient() {
        val user = sessionManager.userInfo ?: throw IllegalArgumentException()

        launchAndExecuteRequest(
            request = {
                usersService.getMonitorOfClient(UUID.fromString(user.id), user.token)
            },
            onSuccess = {
                _monitor.value = it
            }
        )
    }

    fun getClientsOfMonitor() {
        val user = sessionManager.userInfo ?: throw IllegalArgumentException()

        launchAndExecuteRequest(
            request = {
                usersService.getClientsOfMonitor(UUID.fromString(user.id), user.token)
            },
            onSuccess = {
                _clients.value = it
            }
        )
    }

}