package pt.ipc_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipc_app.service.UsersService
import pt.ipc_app.session.SessionManagerSharedPrefs

/**
 * View model for the [MainActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class RegisterViewModel(
    val usersService: UsersService,
    val sessionManager: SessionManagerSharedPrefs
) : ViewModel() {

    /**
     * Attempts to register the user with the given credentials.
     *
     * @param email the email of the user
     * @param name the name of the user
     * @param password the password of the user
     */
    fun register(
        email: String,
        name: String,
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
                weight = weight,
                height = height,
                birthDate = birthDate,
                physicalCondition = physicalCondition
            )
        }
    }
}