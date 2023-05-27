package pt.ipc_app.ui.screens.search_clients

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.util.UUID

/**
 * View model for the [SearchClientsActivity].
 */
class RegisterViewModel(
    private val usersService: UsersService
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    /**
     * Attempts to connect the monitor with a client.
     */
    fun connectWithClient(
        clientId: UUID
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.connectClient(
                    clientId
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }
            },
            onSuccess = {

            }
        )
    }
}