package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.ui.screens.details.ClientDetailsActivity
import pt.ipc_app.ui.screens.plan.CreatePlanActivity
import pt.ipc_app.ui.screens.userInfo.MonitorInfoActivity
import pt.ipc_app.utils.viewModelInit
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
        setContent {
            var clientsA by remember { mutableStateOf(clients.clients) }
            var requestsA by remember { mutableStateOf(requests.requests) }

            MonitorHomeScreen(
                monitor = repo.userInfo!!,
                clientsOfMonitor = clientsA,
                requestsOfMonitor = requestsA,
                onClientSelected = { ClientDetailsActivity.navigate(this, it) },
                onClientRequestAccepted = { request, decision ->
                    viewModel.decideConnectionRequestOfClient(request.requestID, decision)
                    clientsA = clientsA + ClientOutput(request.clientID, request.clientName, request.clientEmail)
                    requestsA = requestsA - requestsA.first {it.requestID == request.requestID}
                },
                onPlansRequest = { CreatePlanActivity.navigate(this) },
                onUserInfoClick = { MonitorInfoActivity.navigate(this) }
            )
        }
    }

    @Suppress("deprecation")
    private val clients: ClientsOfMonitor by lazy {
        val clients = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(CLIENTS, ClientsOfMonitor::class.java)
        else
            intent.getParcelableExtra(CLIENTS)
        checkNotNull(clients)
    }

    @Suppress("deprecation")
    private val requests: RequestsOfMonitor by lazy {
        val requests = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(REQUESTS, RequestsOfMonitor::class.java)
        else
            intent.getParcelableExtra(REQUESTS)
        checkNotNull(requests)
    }
}