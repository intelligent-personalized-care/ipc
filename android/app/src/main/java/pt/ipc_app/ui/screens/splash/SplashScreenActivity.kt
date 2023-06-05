package pt.ipc_app.ui.screens.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import kotlinx.coroutines.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.isClient
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.ui.screens.role.ChooseRoleActivity
import pt.ipc_app.utils.viewModelInit
import java.util.*

/**
 * The start screen.
 */
class SplashScreenActivity: ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<SplashScreenViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            SplashScreenViewModel(app.services.usersService, app.sessionManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
/*
        repo.setSession(
            id = UUID.fromString("b48faf56-2c85-455d-98ea-97ee4fcdf97e"),
            name = "Tiago",
            token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyRW1haWwiOiJtb25pdG9yQGdtYWlsLmNvbSIsInVzZXJJRCI6ImI0OGZhZjU2LTJjODUtNDU1ZC05OGVhLTk3ZWU0ZmNkZjk3ZSIsInJvbGUiOiJNT05JVE9SIn0.DtjpwWywt8thg0eZf9dY-r5EYq2dAiYDkCsTvZcZ6AHJg3bioSJBHRoj-U9T9beo8Cn-_HiT1e_qjbUVqh0N0A",
            role = Role.MONITOR
        )

 */
        repo.clearSession()

        CoroutineScope(Dispatchers.Main).launch {
            repo.userInfo?.let {
                if (it.role.isClient()) {
                    viewModel.getMonitorOfClient()
                    viewModel.getCurrentPlanOfClient()
                } else {
                    viewModel.getClientsOfMonitor()
                }
            }

            delay(3000)

            if (!repo.isLoggedIn()) {
                ChooseRoleActivity.navigate(this@SplashScreenActivity)
            } else {
                if (repo.userInfo!!.role.isClient()) {
                    ClientHomeActivity.navigate(this@SplashScreenActivity, viewModel.monitor.value, viewModel.plan.value)
                } else {
                    MonitorHomeActivity.navigate(this@SplashScreenActivity, viewModel.clients.value)
                }
            }
            finish()
        }
    }
}