package pt.ipc_app.ui.screens.profile

import android.content.Context
import coil.request.ImageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File

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

    private val _clientProfile = MutableStateFlow<ClientOutput?>(null)
    val clientProfile
        get() = _clientProfile.asStateFlow()

    fun getProfilePicture(
        context: Context
    ): ImageRequest =
        usersService.getProfilePicture(
            context = context,
            userId = sessionManager.userUUID,
            token = sessionManager.userLoggedIn.token
        )

    /**
     * Attempts to get the profile of client.
     */
    fun getProfile() {
        launchAndExecuteRequest(
            request = {
                usersService.getClientProfile(
                    clientId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.token
                )
            },
            onSuccess = {
                _clientProfile.value = it
            }
        )
    }

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
                    userId = sessionManager.userUUID,
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