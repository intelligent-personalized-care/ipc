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
            token = sessionManager.userLoggedIn.token
        )

    /**
     * Attempts to get the client details.
     */
    fun getClientDetails(
        clientId: UUID
    ) {
        val user = sessionManager.userLoggedIn

        launchAndExecuteRequest(
            request = {
                usersService.getClientOfMonitor(UUID.fromString(user.id), clientId, user.token)
            },
            onSuccess = {
                _client.value = it
            }
        )
    }

    /**
     * Attempts to get the monitor plans.
     */
    fun getMonitorPlans(
        monitorId: UUID
    ) {
        launchAndExecuteRequest(
            request = {
                plansService.getMonitorPlans(
                    monitorId = monitorId,
                    token = sessionManager.userLoggedIn.token
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
        monitorId: UUID,
        clientId: UUID,
        planId: Int,
        startDate: String
    ) {
        launchAndExecuteRequest(
            request = {
                plansService.associatePlanToClient(
                    monitorId = monitorId,
                    clientId = clientId,
                    token = sessionManager.userLoggedIn.token,
                    planId = planId,
                    startDate = startDate
                )
            },
            onSuccess = { }
        )
    }
}