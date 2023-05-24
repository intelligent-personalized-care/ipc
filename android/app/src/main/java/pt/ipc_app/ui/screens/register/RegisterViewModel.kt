package pt.ipc_app.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [RegisterActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.Idle)
    val state
        get() = _state.asStateFlow()

    private val _chosenRole = MutableStateFlow<Role?>(null)
    val chosenRole
        get() = _chosenRole.asStateFlow()


    fun selectRole(role: Role?) {
        _chosenRole.value = role
    }

    /**
     * Attempts to register the client with the given credentials.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    fun registerClient(
        name: String,
        email: String,
        password: String,
        weight: Int,
        height: Int,
        birthDate: String,
        physicalCondition: String
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.Creating
                usersService.registerClient(
                    name = name,
                    email = email,
                    password = password,
                    weight = if (weight != 0) weight else null,
                    height = if (height != 0) height else null,
                    birthDate = birthDate.ifEmpty { null },
                    physicalCondition = physicalCondition.ifEmpty { null }
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.Created else ProgressState.Idle
                }
            },
            onSuccess = {
                sessionManager.setSession(name, it.token, Role.CLIENT)
            }
        )
    }

    /**
     * Attempts to register the monitor with the given credentials.
     *
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    fun registerMonitor(
        name: String,
        email: String,
        password: String,
        credential: ByteArray
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.Creating
                usersService.registerMonitor(
                    name = name,
                    email = email,
                    password = password,
                    credential = credential
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.Created else ProgressState.Idle
                }
            },
            onSuccess = {
                sessionManager.setSession(name, it.token, Role.MONITOR)
            }
        )
    }
}