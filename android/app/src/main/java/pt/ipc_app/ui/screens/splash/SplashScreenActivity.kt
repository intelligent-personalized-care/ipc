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

        repo.setSession(
            id = "6acf5755-a520-4fda-bd99-69b9fdc6b1eb",
            name = "Client",
            token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyRW1haWwiOiJ0ZXN0ZWVlQGdtYWlsLmNvbSIsInVzZXJJRCI6IjZhY2Y1NzU1LWE1MjAtNGZkYS1iZDk5LTY5YjlmZGM2YjFlYiIsInJvbGUiOiJDTElFTlQifQ.NoBC5Wx_FkXQYVsbEk71jeBpd-4BHGNAGnNYojV6JR7cQse7WWiGUl9jiAZ50bvhXwX3B_0zvlTSaghBCU_6Vg",
            role = Role.CLIENT
        )

        //repo.clearSession()
        //Log.println(Log.WARN, "TOKEN", repo.userInfo!!.token)

        // CLIENT: eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyRW1haWwiOiJjbGllbnQxQGdtYWlsLmNvbSIsInVzZXJJRCI6IjRlYWUzZDVlLTNiMzAtNGM3Zi1iMWJmLTU4NDEzMTQ3YmY2NiIsInJvbGUiOiJDTElFTlQifQ.hMt5lBZzCFnIG7OQyZpd2TGNevRHermn9KVsMvPImquBnLh2xdy8APfbBXTF663Ia-Ml2Ra70IQsBgvHrUZ0Jw

        CoroutineScope(Dispatchers.Main).launch {
            repo.userInfo?.let {
                if (it.role.isClient()) {
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
                if (repo.userInfo!!.role.isClient()) {
                    ClientHomeActivity.navigate(this@SplashScreenActivity, viewModel.monitor.value, viewModel.plan.value)
                } else {
                    MonitorHomeActivity.navigate(this@SplashScreenActivity, viewModel.clients.value, viewModel.requests.value)
                }
            }
            finish()
        }
    }
}