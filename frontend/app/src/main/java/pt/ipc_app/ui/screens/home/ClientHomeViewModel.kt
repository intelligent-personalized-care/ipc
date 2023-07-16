package pt.ipc_app.ui.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.sse.RequestAcceptance
import pt.ipc_app.service.models.sse.SseEvent
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.sse.EventBus
import pt.ipc_app.service.sse.SseEventListener
import pt.ipc_app.preferences.SessionManagerSharedPrefs
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
) : AppViewModel(), SseEventListener {

    private val _monitor = MutableStateFlow<MonitorOutput?>(null)
    val monitor
        get() = _monitor.asStateFlow()

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan
        get() = _plan.asStateFlow()

    fun getCurrentPlanOfClient(date: LocalDate = LocalDate.now()) {
        launchAndExecuteRequest(
            request = {
                usersService.getCurrentPlanOfClient(sessionManager.userUUID, date, sessionManager.userLoggedIn.accessToken)
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

    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        if (eventData is RequestAcceptance)
            _monitor.value = eventData.monitor
    }

}