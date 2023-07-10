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
            SplashScreenViewModel(app.services.usersService, app.services.sseService, app.sessionManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        repo.setSession(
            id = "78d82d8d-36a5-4f15-bc71-b846887555fe",
            name = "Tiago",
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiI3OGQ4MmQ4ZC0zNmE1LTRmMTUtYmM3MS1iODQ2ODg3NTU1ZmUiLCJyb2xlIjoiTU9OSVRPUiIsInNlc3Npb25JRCI6IjdhM2FmZmQ5LTZjMmItNDQ0NS1iNmE5LTVkM2U0ODc1MWZmZiIsImV4cCI6MTY4OTA1OTc1MH0.m9-SzUDLkIIlJsHjBOLC1P5zxZywP1b7L_zS50JCec-hGtYTRIblrmg6t-0Wes7WY4eGfw3i3_4ELKPxvsFBHw",
            refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzZXNzaW9uSUQiOiI2YzU0NjcwZC05MmE0LTQ0MTAtYjIyOS1lZjZlZjQ3ZTI4OTAiLCJleHAiOjE2ODkwMDgxMDR9.5eYLo7JshEfJtPABjYzNgQebqMrvBjyRG5UGSqTeXqjUbkULPMrpxc5VhRix7Eh8cBVXlpoPLogSP6tpUp2gIA",
            role = Role.MONITOR
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

            delay(SPLASH_TIME)

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

    companion object {
        const val SPLASH_TIME = 3000L
    }
}