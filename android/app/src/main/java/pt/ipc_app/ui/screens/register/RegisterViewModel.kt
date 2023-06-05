package pt.ipc_app.ui.screens.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File

/**
 * View model for the [RegisterActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
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
                _state.value = ProgressState.WAITING
                usersService.registerClient(
                    name = name,
                    email = email,
                    password = password,
                    weight = if (weight != 0) weight else null,
                    height = if (height != 0) height else null,
                    birthDate = birthDate.ifEmpty { null },
                    physicalCondition = physicalCondition.ifEmpty { null }
                ).also {
                    if (it !is APIResult.Success) _state.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                sessionManager.setSession(it.id, name, it.token, Role.CLIENT)
                _state.value = ProgressState.FINISHED
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
        password: String
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                usersService.registerMonitor(
                    name = name,
                    email = email,
                    password = password
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }
            },
            onSuccess = {
                sessionManager.setSession(it.id, name, it.token, Role.MONITOR)
            }
        )
    }
}