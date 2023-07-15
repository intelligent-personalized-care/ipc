package pt.ipc_app.ui.screens.details

import android.content.Context
import coil.request.ImageRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.PlansService
import pt.ipc_app.service.UsersService
import pt.ipc_app.service.models.plans.ListOfPlans
import pt.ipc_app.service.models.users.ClientOfMonitor
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [ClientDetailsActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ClientDetailsViewModel(
    private val plansService: PlansService,
    private val usersService: UsersService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _client = MutableStateFlow<ClientOfMonitor?>(null)
    val client
        get() = _client.asStateFlow()

    private val _plans = MutableStateFlow(ListOfPlans(listOf()))
    val plans
        get() = _plans.asStateFlow()

    fun getProfilePicture(
        context: Context,
        clientId: UUID
    ): ImageRequest =
        usersService.getProfilePicture(
            context = context,
            userId = clientId,
            token = sessionManager.userLoggedIn.accessToken
        )

    /**
     * Attempts to get the client details.
     */
    fun getClientDetails(
        clientId: UUID
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.getClientOfMonitor(sessionManager.userUUID, clientId, sessionManager.userLoggedIn.accessToken)
            },
            onSuccess = {
                _client.value = it
            }
        )
    }

    /**
     * Attempts to get the monitor plans.
     */
    fun getMonitorPlans() {
        launchAndExecuteRequest(
            request = {
                plansService.getMonitorPlans(
                    monitorId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = {
                _plans.value = it
            }
        )
    }

    /**
     * Attempts to associate a plan to a client.
     */
    fun associatePlanToClient(
        clientId: UUID,
        planId: Int,
        startDate: String
    ) {
        launchAndExecuteRequest(
            request = {
                plansService.associatePlanToClient(
                    monitorId = sessionManager.userUUID,
                    clientId = clientId,
                    token = sessionManager.userLoggedIn.accessToken,
                    planId = planId,
                    startDate = startDate
                )
            }
        )
    }

    /**
     * Attempts to disconnects a client from his monitor.
     */
    fun disconnectMonitor(
        clientId: UUID
    ) {
        launchAndExecuteRequest(
            request = {
                usersService.disconnectMonitor(
                    monitorId = sessionManager.userUUID,
                    clientId = clientId,
                    token = sessionManager.userLoggedIn.accessToken
                )
            }
        )
    }
}