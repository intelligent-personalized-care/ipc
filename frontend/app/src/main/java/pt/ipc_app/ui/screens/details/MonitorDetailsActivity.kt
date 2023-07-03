package pt.ipc_app.ui.screens.details

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.openSendEmail
import pt.ipc_app.utils.viewModelInit

/**
 * The monitor details activity.
 */
class MonitorDetailsActivity : ComponentActivity() {

    private val viewModel by viewModels<MonitorDetailsViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            MonitorDetailsViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        const val MONITOR = "MONITOR"
        fun navigate(context: Context, monitor: MonitorOutput) {
            with(context) {
                val intent = Intent(this, MonitorDetailsActivity::class.java)
                intent.putExtra(MONITOR, monitor)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitorDetailsScreen(
                monitor = monitor,
                profilePictureUrl = viewModel.getProfilePictureUrl(monitor.id),
                onSendEmailRequest = { openSendEmail(monitor.email) },
                onRequestedConnection = { viewModel.connectWithMonitor(monitor.id) }
            )
        }
    }

    @Suppress("deprecation")
    private val monitor: MonitorOutput by lazy {
        val monitor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(MONITOR, MonitorOutput::class.java)
        else
            intent.getParcelableExtra(MONITOR)
        checkNotNull(monitor)
    }
}