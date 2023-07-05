package pt.ipc_app.ui.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.DailyList
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.time.LocalDate

/**
 * View model for the [ClientHomeActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ClientHomeViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _monitor = MutableStateFlow<MonitorOutput?>(null)
    val monitor
        get() = _monitor.asStateFlow()

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan
        get() = _plan.asStateFlow()

    fun getCurrentPlanOfClient() {
        launchAndExecuteRequest(
            request = {
                usersService.getCurrentPlanOfClient(sessionManager.userUUID, sessionManager.userLoggedIn.token)
            },
            onSuccess = {
                _plan.value = it
            }
        )
    }

    fun getMonitorOfClient() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorOfClient(sessionManager.userUUID, sessionManager.userLoggedIn.token)
            },
            onSuccess = {
                _monitor.value = it
            }
        )
    }

}