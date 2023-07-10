package pt.ipc_app.ui.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.exercises.ExercisesOfClients
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.time.LocalDate
import java.util.*

/**
 * View model for the [MonitorHomeActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class MonitorHomeViewModel(
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _clients = MutableStateFlow<ClientsOfMonitor?>(null)
    val clients
        get() = _clients.asStateFlow()

    private val _clientsExercises = MutableStateFlow<ExercisesOfClients?>(null)
    val clientsExercises
        get() = _clientsExercises.asStateFlow()

    private val _clientsExercisesState = MutableStateFlow(ProgressState.IDLE)
    val clientsExercisesState
        get() = _clientsExercisesState.asStateFlow()

    private val _requests = MutableStateFlow<RequestsOfMonitor?>(null)
    val requests
        get() = _requests.asStateFlow()

    fun getClientsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getClientsOfMonitor(sessionManager.userUUID, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _clients.value = it
            }
        )
    }

    fun getRequestsOfMonitor() {
        launchAndExecuteRequest(
            request = {
                usersService.getMonitorRequests(sessionManager.userUUID, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _requests.value = it
            }
        )
    }

    fun getExercisesOfClients(
        date: LocalDate
    ) {
        launchAndExecuteRequest(
            request = {
                _clientsExercisesState.value = ProgressState.WAITING
                usersService.getExercisesOfClients(sessionManager.userUUID, date, sessionManager.userLoggedIn.accessToken).also {
                    if (it !is APIResult.Success) _clientsExercisesState.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _clientsExercises.value = it
                _clientsExercisesState.value = ProgressState.FINISHED
            }
        )
    }

    fun decideConnectionRequestOfClient(
        requestId: UUID,
        requestDecision: ConnectionRequestDecisionInput
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.decideConnectionRequest(
                    monitorId = sessionManager.userUUID,
                    requestId = requestId,
                    requestDecision = requestDecision,
                    token = sessionManager.userLoggedIn.accessToken
                )
            }
        )
    }

}