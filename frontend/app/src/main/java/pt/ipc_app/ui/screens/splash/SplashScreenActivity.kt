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
            SplashScreenViewModel(app.services.usersService, app.sessionManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
/*
        repo.setSession(
            id = "a5e102e5-aed8-412e-b9fd-b8b1cbe2d228",
            name = "Tiago",
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiJhNWUxMDJlNS1hZWQ4LTQxMmUtYjlmZC1iOGIxY2JlMmQyMjgiLCJyb2xlIjoiQ0xJRU5UIiwic2Vzc2lvbklEIjoiNmM1NDY3MGQtOTJhNC00NDEwLWIyMjktZWY2ZWY0N2UyODkwIiwiZXhwIjoxNjg5MDA4MTA0fQ.kd4lh-ibNpWqwqbWYBaeLC1klyVmMuW_0CoPY2tjlIs7C7rZZaKHaCpmpU-KyfroFmap06eqb8E1gxqSuvEiRA",
            refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzZXNzaW9uSUQiOiI2YzU0NjcwZC05MmE0LTQ0MTAtYjIyOS1lZjZlZjQ3ZTI4OTAiLCJleHAiOjE2ODkwMDgxMDR9.5eYLo7JshEfJtPABjYzNgQebqMrvBjyRG5UGSqTeXqjUbkULPMrpxc5VhRix7Eh8cBVXlpoPLogSP6tpUp2gIA",
            role = Role.MONITOR
        )

 */

        repo.setSession(
            id = "ba5117d7-f202-4ba2-ab7f-364f1fbae149",
            name = "Guilherme",
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiJiYTUxMTdkNy1mMjAyLTRiYTItYWI3Zi0zNjRmMWZiYWUxNDkiLCJyb2xlIjoiQ0xJRU5UIiwic2Vzc2lvbklEIjoiM2RmNTcyNWQtNWUyOC00YjdhLWI1MDgtNTQyN2M0MjNlMjdiIiwiZXhwIjoxNjg5MDEyMDc1fQ.97guPe2au4STP0VyyIaGL31aVSnj27U9IuHuZXcVgmntM-idl3HtyX3KyjuxFxfgGn1sudkChEdms5UG89WhEg",
            refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzZXNzaW9uSUQiOiIzZGY1NzI1ZC01ZTI4LTRiN2EtYjUwOC01NDI3YzQyM2UyN2IiLCJleHAiOjE2ODkwMTIwNzV9.4g6nmMB-KiWpJzUobTO_6ngmfVLJfV0--g3idYSEL5WMbxWvecQCQgM_j20HGMl3KKdORNVFOPgmx2cesD1ErQ",
            role = Role.CLIENT
        )

        //repo.clearSession()
        CoroutineScope(Dispatchers.Main).launch {
            //viewModel.subscribeToServerSendEvents()
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