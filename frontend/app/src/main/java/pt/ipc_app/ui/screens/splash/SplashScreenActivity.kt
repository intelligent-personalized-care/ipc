package pt.ipc_app.ui.screens.splash

import android.os.Bundle
import android.util.Log
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
            id = "54729911-225f-41dc-a6de-a230bf6e0c07",
            name = "Tiago",
            token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiI1NDcyOTkxMS0yMjVmLTQxZGMtYTZkZS1hMjMwYmY2ZTBjMDciLCJyb2xlIjoiTU9OSVRPUiJ9.pX37fZ0lF1EINdtu-4719NCVdNJxGdNY3076FdTIPDtGm1Orz3cdgVgUBHsSYnriicqc5ngeOE5C2OcNV2Dbmw",
            role = Role.MONITOR
        )
         */
        repo.setSession(
            id = "9eb13cff-28a6-4510-880e-6b64a7a4b7f5",
            name = "Tiago",
            token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiI5ZWIxM2NmZi0yOGE2LTQ1MTAtODgwZS02YjY0YTdhNGI3ZjUiLCJyb2xlIjoiQ0xJRU5UIn0.cRXp0VeFbHtald-QHR3znL2pxOipQDBv2_umt8y2iAYq0PWFdo3EjmLv_R0kxtUP4BsnVxvfnKHrL_0kTcNqFA",
            role = Role.CLIENT
        )
        CoroutineScope(Dispatchers.Main).launch {
            if (repo.isLoggedIn()) {
                if (repo.userLoggedIn.role.isClient()) {
                    viewModel.getMonitorOfClient()
                    viewModel.getCurrentPlanOfClient()
                } else {
                    viewModel.getClientsOfMonitor()
                    viewModel.getRequestsOfMonitor()
                }
            }

            delay(3000)

            if (!repo.isLoggedIn()) {
                ChooseRoleActivity.navigate(this@SplashScreenActivity)
            } else {
                if (repo.userLoggedIn.role.isClient()) {
                    ClientHomeActivity.navigate(this@SplashScreenActivity, viewModel.monitor.value, viewModel.plan.value)
                } else {
                    MonitorHomeActivity.navigate(this@SplashScreenActivity, viewModel.clients.value, viewModel.requests.value)
                }
            }
            finish()
        }
    }
}