package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.ui.screens.details.ClientDetailsActivity
import pt.ipc_app.ui.screens.plan.CreatePlanActivity
import pt.ipc_app.ui.screens.userInfo.MonitorInfoActivity
import java.util.*

/**
 * The monitor home activity.
 */
class MonitorHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    companion object {
        const val CLIENTS = "CLIENTS"
        fun navigate(context: Context, clientsOfMonitor: ClientsOfMonitor? = null) {
            with(context) {
                val intent = Intent(this, MonitorHomeActivity::class.java)
                intent.putExtra(CLIENTS, clientsOfMonitor)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitorHomeScreen(
                monitor = repo.userInfo!!,
                clientsOfMonitor = clients.clients,
                onClientSelected = { ClientDetailsActivity.navigate(this, it) },
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
}