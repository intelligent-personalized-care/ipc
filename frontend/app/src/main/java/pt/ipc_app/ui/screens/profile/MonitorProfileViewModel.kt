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
 * View model for the [MonitorProfileActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorProfileViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    enum class FileToSubmit { PICTURE, CREDENTIAL}

    private var _fileToSubmit = MutableStateFlow<FileToSubmit?>(null)
    val fileToSubmit
        get() = _fileToSubmit.asStateFlow()

    private val _documentState = MutableStateFlow(ProgressState.IDLE)
    val documentState
        get() = _documentState.asStateFlow()

    private val _pictureState = MutableStateFlow(ProgressState.IDLE)
    val pictureState
        get() = _pictureState.asStateFlow()

    fun getProfilePictureUrl(): String =
        usersService.getProfilePictureUrl(UUID.fromString(sessionManager.userLoggedIn.id))

    fun setFileToSubmit(type: FileToSubmit) {
        _fileToSubmit.value = type
    }

    /**
     * Attempts to update the profile picture of client.
     */
    fun updatePicture(
        image: File
    ) {
        launchAndExecuteRequest(
            request = {
                _pictureState.value = ProgressState.WAITING
                usersService.updateProfilePicture(
                    image = image,
                    userId = UUID.fromString(sessionManager.userLoggedIn.id),
                    role = Role.MONITOR,
                    token = sessionManager.userLoggedIn.token
                ).also {
                    if (it !is APIResult.Success) _pictureState.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _pictureState.value = ProgressState.FINISHED
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
                _documentState.value = ProgressState.WAITING
                usersService.submitCredentialDocument(
                    doc = doc,
                    monitorId = UUID.fromString(sessionManager.userLoggedIn.id),
                    token = sessionManager.userLoggedIn.token
                ).also {
                    if (it !is APIResult.Success) _documentState.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _documentState.value = ProgressState.FINISHED
            }
        )
    }
}