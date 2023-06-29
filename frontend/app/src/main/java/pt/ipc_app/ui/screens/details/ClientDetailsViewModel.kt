package pt.ipc_app.ui.screens.details

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.service.PlansService
import pt.ipc_app.service.models.plans.ListOfPlans
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
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _plans = MutableStateFlow(ListOfPlans(listOf()))
    val plans
        get() = _plans.asStateFlow()

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