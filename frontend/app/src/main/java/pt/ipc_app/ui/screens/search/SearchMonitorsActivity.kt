package pt.ipc_app.ui.screens.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.R
import pt.ipc_app.ui.screens.details.MonitorDetailsActivity
import pt.ipc_app.ui.setAppContentClient
import pt.ipc_app.utils.viewModelInit

/**
 * The search monitors activity.
 */
class SearchMonitorsActivity : ComponentActivity() {

    private val viewModel by viewModels<SearchMonitorsViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            SearchMonitorsViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, SearchMonitorsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContentClient(viewModel) {

            SearchMonitorsScreen(
                monitors = viewModel.monitors.collectAsState().value,
                requestState = viewModel.state.collectAsState().value,
                onSearchRequest = { viewModel.searchMonitors(it) },
                onMonitorClick = { MonitorDetailsActivity.navigate(this, it) }
            )
        }
    }
}