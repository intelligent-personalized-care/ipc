package pt.ipc_app.ui.screens.login

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
import pt.ipc_app.domain.user.isClient
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.utils.viewModelInit

/**
 * The login activity.
 */
class LoginActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<LoginViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            LoginViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repo.clearSession()
        setContent {
            val state by viewModel.state.collectAsState()

            LoginScreen(
                progressState = state,
                error = viewModel.error,
                onSaveRequest = { email, password ->
                    viewModel.login(email, password)
                }
            )
        }

        lifecycleScope.launch {
            viewModel.state.collect {

                if (it == ProgressState.FINISHED) {
                    if (repo.userLoggedIn.role.isClient())
                        ClientHomeActivity.navigate(this@LoginActivity)
                    else
                        MonitorHomeActivity.navigate(this@LoginActivity)
                    finish()
                }
            }
        }
    }

}