package pt.ipc_app.ui.screens.userInfo

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File
import java.util.*

/**
 * View model for the [MonitorInfoActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorInfoViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    /**
     * Attempts to update the profile picture of client.
     */
    fun updatePicture(
        image: File
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.updateProfilePicture(
                    image = image,
                    clientId = UUID.fromString(sessionManager.userInfo!!.id),
                    token = sessionManager.userInfo!!.token
                ).also {
                    if (it !is APIResult.Success) _state.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
            }
        )
    }

    /**
     * Attempts to submit the credential document of monitor.
     */
    fun submitCredentialDocument(
        doc: File
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.submitCredentialDocument(
                    doc = doc,
                    monitorId = UUID.fromString(sessionManager.userInfo!!.id),
                    token = sessionManager.userInfo!!.token
                ).also {
                    if (it !is APIResult.Success) _state.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
            }
        )
    }
}