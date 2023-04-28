package pt.ipc_app.ui.screens.register

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.user.Role
import pt.ipc_app.utils.viewModelInit

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
            if (Role.isClient(role))
                RegisterClientScreen(
                    onSaveRequest = {
                        viewModel.registerClient(
                            it.name, it.email, it.password, it.weight, it.height, it.birthDate, it.physicalCondition
                        )
                    }
                )
            else
                RegisterMonitorScreen(
                    onSaveRequest = {
                        viewModel.registerMonitor(
                            it.name, it.email, it.password, it.credential!!
                        )
                    }
                )
        }
    }

    override fun onStop() {
        super.onStop()
        println("-------------------------------------------------------------------------------------")
    }

    @Suppress("deprecation")
    private val role: String by lazy {
        val role = intent.getStringExtra(ROLE_CHOSE)
        checkNotNull(role)
    }

}