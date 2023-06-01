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

        repo.setSession(
            id = "0e2843b0-8010-4ac2-970b-dd05a5dd7d81",
            name = "Tiago",
            token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyRW1haWwiOiJ0ZXN0ZTEyM0BnbWFpbC5jb20iLCJ1c2VySUQiOiIwZTI4NDNiMC04MDEwLTRhYzItOTcwYi1kZDA1YTVkZDdkODEiLCJyb2xlIjoiQ0xJRU5UIn0.03Q5Z_XKt-jPNVqyuelpm-zl5zaKdU8VTdHMzpRTC8jTFR8nvmKeNpBkg9HE4DiANEPfrLZC2vyydv-G0w-Jcg",
            role = Role.CLIENT
        )

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.getMonitorOfClient()
            viewModel.getCurrentPlanOfClient()

            delay(3000)

            if (!repo.isLoggedIn()) {
                ChooseRoleActivity.navigate(this@SplashScreenActivity)
            } else {
                if (repo.userInfo?.role!!.isClient()) {
                    ClientHomeActivity.navigate(this@SplashScreenActivity, viewModel.monitor.value, viewModel.plan.value)
                } else {
                    MonitorHomeActivity.navigate(this@SplashScreenActivity)
                }
            }
            finish()
        }
    }
}