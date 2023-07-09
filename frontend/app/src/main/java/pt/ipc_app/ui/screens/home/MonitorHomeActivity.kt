package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.ui.screens.details.ClientDetailsActivity
import pt.ipc_app.ui.screens.plan.PlanActivity
import pt.ipc_app.ui.setAppContentMonitor
import pt.ipc_app.utils.viewModelInit
import java.time.LocalDate
import java.util.*

/**
 * The monitor home activity.
 */
class MonitorHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<MonitorHomeViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            MonitorHomeViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        const val CLIENTS = "CLIENTS"
        const val REQUESTS = "REQUESTS"
        fun navigate(context: Context, clientsOfMonitor: ClientsOfMonitor? = null, requestsOfMonitor: RequestsOfMonitor? = null) {
            with(context) {
                val intent = Intent(this, MonitorHomeActivity::class.java)
                intent.putExtra(CLIENTS, clientsOfMonitor)
                intent.putExtra(REQUESTS, requestsOfMonitor)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (clients == null) viewModel.getClientsOfMonitor()
        if (requests == null) viewModel.getRequestsOfMonitor()
        viewModel.getExercisesOfClients(LocalDate.now())

        setAppContentMonitor(viewModel) {

            var clientsList by remember { mutableStateOf(clients?.clients) }
            var requestsList by remember { mutableStateOf(requests?.requests) }

            if (clientsList == null)
                clientsList = viewModel.clients.collectAsState().value?.clients
            if (requestsList == null)
                requestsList = viewModel.requests.collectAsState().value?.requests

            MonitorHomeScreen(
                monitor = repo.userLoggedIn,
                clientsOfMonitor = clientsList ?: listOf(),
                requestsOfMonitor = requestsList ?: listOf(),
                clientsExercisesToDo = viewModel.clientsExercises.collectAsState().value?.clientsExercises ?: listOf(),
                clientsExercisesToDoProgressState = viewModel.clientsExercisesState.collectAsState().value,
                onDaySelected = { viewModel.getExercisesOfClients(it) },
                onClientSelected = { ClientDetailsActivity.navigate(this, it) },
                onClientRequestDecided = { request, decision ->
                    viewModel.decideConnectionRequestOfClient(request.requestID, decision)

                    if (decision.accept)
                        clientsList = (clientsList ?: listOf()) + ClientOutput(request.clientID, request.clientName, request.clientEmail)

                    requestsList?.let { reqs ->
                        requestsList = reqs - reqs.first {it == request}
                    }
                },
                onClientExercisesSelected = { clientId, clientName, planDate ->
                    PlanActivity.navigate(this, clientId, clientName, planDate.toString())
                }
            )
        }
    }

    @Suppress("deprecation")
    private val clients: ClientsOfMonitor? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(CLIENTS, ClientsOfMonitor::class.java)
        else
            intent.getParcelableExtra(CLIENTS)
    }

    @Suppress("deprecation")
    private val requests: RequestsOfMonitor? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(REQUESTS, RequestsOfMonitor::class.java)
        else
            intent.getParcelableExtra(REQUESTS)
    }
}