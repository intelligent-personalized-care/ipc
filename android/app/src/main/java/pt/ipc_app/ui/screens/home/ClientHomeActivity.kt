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
import pt.ipc_app.domain.user.*
import pt.ipc_app.ui.components.plan
import pt.ipc_app.ui.screens.exercise.ExerciseActivity
import pt.ipc_app.ui.screens.splash.ClientHomeViewModel
import pt.ipc_app.utils.viewModelInit

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
        setContent {
            ClientHomeScreen(
                client = repo.userInfo!!,
                monitor = Monitor("Miguel", "miguel@gmail.com", "Aa123456@", null, "Physiotherapist"),
                plan =  plan,//viewModel.plan.collectAsState().value,
                onExerciseSelect = { ExerciseActivity.navigate(this, it) }
            )
        }

        lifecycleScope.launch {
            viewModel.getCurrentPlanOfClient()
        }
    }
}