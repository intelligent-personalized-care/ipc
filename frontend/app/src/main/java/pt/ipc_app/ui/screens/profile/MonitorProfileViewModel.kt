package pt.ipc_app.ui.screens.profile

import android.content.Context
import coil.request.ImageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.sse.CredentialAcceptance
import pt.ipc_app.service.models.sse.SseEvent
import pt.ipc_app.service.models.users.DocState
import pt.ipc_app.service.models.users.MonitorProfile
import pt.ipc_app.service.sse.EventBus
import pt.ipc_app.service.sse.SseEventListener
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File

/**
 * View model for the [MonitorProfileActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorProfileViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel(), SseEventListener {

    enum class FileToSubmit { PICTURE, CREDENTIAL}

    private var _fileToSubmit = MutableStateFlow<FileToSubmit?>(null)
    val fileToSubmit
        get() = _fileToSubmit.asStateFlow()

    private val _monitorProfile = MutableStateFlow<MonitorProfile?>(null)
    val monitorProfile
        get() = _monitorProfile.asStateFlow()

    private val _documentState = MutableStateFlow(ProgressState.IDLE)
    val documentState
        get() = _documentState.asStateFlow()

    private val _pictureState = MutableStateFlow(ProgressState.IDLE)
    val pictureState
        get() = _pictureState.asStateFlow()

    fun setFileToSubmit(type: FileToSubmit) {
        _fileToSubmit.value = type
    }

    fun setDocumentSubmitted() {
        _monitorProfile.value = monitorProfile.value?.copy(docState = DocState.WAITING.name.lowercase())
    }

    fun getProfilePicture(context: Context): ImageRequest =
        usersService.getProfilePicture(
            context = context,
            userId = sessionManager.userUUID,
            token = sessionManager.userLoggedIn.accessToken
        )

    /**
     * Attempts to get the profile of monitor.
     */
    fun getProfile() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorProfile(
                    monitorId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = {
                _monitorProfile.value = it
            }
        )
    }

    /**
     * Attempts to update the profile picture of monitor.
     */
    fun updatePicture(
        image: File
    ) {
        launchAndExecuteRequest(
            request = {
                _pictureState.value = ProgressState.WAITING
                usersService.updateProfilePicture(
                    image = image,
                    userId = sessionManager.userUUID,
                    role = Role.MONITOR,
                    token = sessionManager.userLoggedIn.accessToken
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
                    monitorId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.accessToken
                ).also {
                    if (it !is APIResult.Success) _documentState.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _documentState.value = ProgressState.FINISHED
            }
        )
    }

    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        if (eventData is CredentialAcceptance && monitorProfile.value != null) {
            _monitorProfile.value = _monitorProfile.value!!.copy(docState = if (eventData.acceptance) DocState.VALID.name.lowercase() else DocState.INVALID.name.lowercase())
        }
    }
}