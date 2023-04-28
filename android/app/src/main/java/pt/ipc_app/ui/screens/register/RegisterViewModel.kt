package pt.ipc_app.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipc_app.service.UsersService
import pt.ipc_app.session.SessionManagerSharedPrefs

/**
 * View model for the [RegisterActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    val usersService: UsersService,
    val sessionManager: SessionManagerSharedPrefs
) : ViewModel() {

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
        viewModelScope.launch {
            usersService.registerClient(
                name = name,
                email = email,
                password = password,
                weight = if (weight != 0) weight else null,
                height = if (height != 0) height else null,
                birthDate = birthDate.ifEmpty { null },
                physicalCondition = physicalCondition.ifEmpty { null }
            )
        }
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
        viewModelScope.launch {
            usersService.registerMonitor(
                name = name,
                email = email,
                password = password,
                credential = credential
            )
        }
    }
}