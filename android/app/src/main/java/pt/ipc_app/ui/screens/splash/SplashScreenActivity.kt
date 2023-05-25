package pt.ipc_app.ui.screens.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.isClient
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.ui.screens.role.ChooseRoleActivity

/**
 * The start screen.
 */
class SplashScreenActivity: ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // suspends the coroutine for 3 seconds

            withContext(Dispatchers.IO) {
                repo.setSession("Tiago", "", Role.CLIENT)
                //repo.clearSession()
                if (!repo.isLoggedIn())
                    ChooseRoleActivity.navigate(this@SplashScreenActivity)
                else {
                    if (repo.userInfo!!.role.isClient())
                        ClientHomeActivity.navigate(this@SplashScreenActivity)
                    else
                        MonitorHomeActivity.navigate(this@SplashScreenActivity)
                }
            }
            finish()
        }
    }
}