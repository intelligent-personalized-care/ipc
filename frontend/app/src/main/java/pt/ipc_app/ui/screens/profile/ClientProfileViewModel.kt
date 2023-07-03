package pt.ipc_app.ui.screens.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File
import java.util.*

/**
 * View model for the [ClientProfileActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ClientProfileViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    fun getProfilePictureUrl(): String =
        usersService.getProfilePictureUrl(UUID.fromString(sessionManager.userLoggedIn.id))

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
                    userId = UUID.fromString(sessionManager.userLoggedIn.id),
                    role = Role.CLIENT,
                    token = sessionManager.userLoggedIn.token
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