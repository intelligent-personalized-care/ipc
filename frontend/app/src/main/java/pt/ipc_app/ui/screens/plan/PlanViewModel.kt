package pt.ipc_app.ui.screens.plan

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.PlansService
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel
import java.time.LocalDate
import java.util.*

/**
 * View model for the [PlanActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class PlanViewModel(
    private val plansService: PlansService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _plan = MutableStateFlow<Plan?>(null)
    val plan
        get() = _plan.asStateFlow()

    /**
     * Attempts to get a plan of client.
     */
    fun getPlanOfClient(
        clientId: String,
        date: String
    ) {
        launchAndExecuteRequest(
            request = {
                plansService.getPlanOfClient(
                    clientId = UUID.fromString(clientId),
                    date = date,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = {
                _plan.value = it
            }
        )
    }

}