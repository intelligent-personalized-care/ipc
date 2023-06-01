package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.plan
import pt.ipc_app.ui.screens.exercise.ExerciseActivity
import pt.ipc_app.ui.screens.info.ClientDetailsActivity
import pt.ipc_app.ui.screens.search.SearchMonitorsActivity
import pt.ipc_app.ui.screens.info.MonitorDetailsActivity
import pt.ipc_app.utils.viewModelInit
import java.util.*

/**
 * The client home activity.
 */
class ClientHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<ClientHomeViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ClientHomeViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, ClientHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userInfo = repo.userInfo!!

        setContent {
            val monitor = viewModel.monitor.collectAsState().value

            ClientHomeScreen(
                client = userInfo,
                monitor = monitor,
                plan = plan,//viewModel.plan.collectAsState().value,
                onMonitorClick = {
                    if (monitor != null)
                        MonitorDetailsActivity.navigate(this, monitor)
                    else
                        SearchMonitorsActivity.navigate(this)
                },
                onExerciseSelect = { ExerciseActivity.navigate(this, it) },
                onUserInfoClick = { ClientDetailsActivity.navigate(this, ClientOutput(UUID.fromString(userInfo.id), userInfo.name, "tiago@gmail.com")) }
            )
        }

        lifecycleScope.launch {
            viewModel.getCurrentPlanOfClient()
            viewModel.getMonitorOfClient()
        }
    }
}