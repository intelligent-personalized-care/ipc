package pt.ipc_app.ui.screens.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [LoginActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class LoginViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    /**
     * Attempts to login the user with the given credentials.
     *
     * @param email the email of the user
     * @param password the password of the user
     */
    fun login(
        email: String,
        password: String
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.login(
                    email = email,
                    password = password
                ).also {
                    if (it !is APIResult.Success) _state.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                sessionManager.setSession(it.id.toString(), it.name, it.token, it.role)
                _state.value = ProgressState.FINISHED
            }
        )
    }
}