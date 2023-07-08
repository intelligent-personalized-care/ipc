package pt.ipc_app.ui.screens.search

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [SearchMonitorsActivity].
 */
class SearchMonitorsViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _monitors = MutableStateFlow(listOf<MonitorOutput>())
    val monitors
        get() = _monitors.asStateFlow()

    /**
     * Attempts to get all monitors available.
     */
    fun searchMonitors(
        name: String? = null
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.searchMonitorsAvailable(
                    name = name,
                    token = sessionManager.userLoggedIn.token
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }

            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
                _monitors.value = it.monitors
            }
        )
    }

}