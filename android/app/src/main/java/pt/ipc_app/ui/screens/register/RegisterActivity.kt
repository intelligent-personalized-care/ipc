package pt.ipc_app.ui.screens.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.user.Role
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.utils.viewModelInit
import java.io.ByteArrayOutputStream

/**
 * The register activity.
 */
class RegisterActivity : ComponentActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            RegisterViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        const val ROLE_CHOSE = "ROLE_CHOSE"
        fun navigate(context: Context, role: String) {
            with(context) {
                val intent = Intent(this, RegisterActivity::class.java)
                intent.putExtra(ROLE_CHOSE, role)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()

            if (Role.isClient(role))
                RegisterClientScreen(
                    progressState = state,
                    error = viewModel.error,
                    onSaveRequest = {
                        viewModel.registerClient(
                            it.name, it.email, it.password, it.weight, it.height, it.birthDate, it.physicalCondition
                        )
                    }
                )
            else
                RegisterMonitorScreen(
                    progressState = state,
                    error = viewModel.error,
                    onFileRequest = { },
                    onSaveRequest = {
                        viewModel.registerMonitor(
                            it.name, it.email, it.password, it.credential!!
                        )
                    }
                )
        }

        lifecycleScope.launch {
            viewModel.state.collect {

                if (it == ProgressState.FINISHED) {
                    if (Role.isClient(role))
                        ClientHomeActivity.navigate(this@RegisterActivity)
                    else
                        MonitorHomeActivity.navigate(this@RegisterActivity)
                    finish()
                }
            }
        }

    }

    @Suppress("deprecation")
    private val role: String by lazy {
        val role = intent.getStringExtra(ROLE_CHOSE)
        checkNotNull(role)
    }

}