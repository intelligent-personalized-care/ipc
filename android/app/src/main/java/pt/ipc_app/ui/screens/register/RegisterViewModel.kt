package pt.ipc_app.ui.screens.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.session.SessionManagerSharedPrefs
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

    private val _registeredRole = MutableStateFlow<Role?>(null)
    val registeredRole
        get() = _registeredRole.asStateFlow()

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
                usersService.registerClient(
                    name = name,
                    email = email,
                    password = password,
                    weight = if (weight != 0) weight else null,
                    height = if (height != 0) height else null,
                    birthDate = birthDate.ifEmpty { null },
                    physicalCondition = physicalCondition.ifEmpty { null }
                )
            },
            onSuccess = {
                val role = Role.CLIENT
                sessionManager.setSession(it.token, name, role)
                _registeredRole.value = role
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
                usersService.registerMonitor(
                    name = name,
                    email = email,
                    password = password,
                    credential = credential
                )
            },
            onSuccess = {
                val role = Role.MONITOR
                sessionManager.setSession(it.token, name, role)
                _registeredRole.value = role
            }
        )
    }
}