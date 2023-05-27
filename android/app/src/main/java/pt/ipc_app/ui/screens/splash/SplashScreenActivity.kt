package pt.ipc_app.ui.screens.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        repo.setSession("8e8aaf90-e3ac-41f6-9569-001eaf10fa68","Tiago", "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyRW1haWwiOiJ0ZXN0ZUBnbWFpbC5jb20iLCJ1c2VySUQiOiI4ZThhYWY5MC1lM2FjLTQxZjYtOTU2OS0wMDFlYWYxMGZhNjgiLCJyb2xlIjoiQ0xJRU5UIn0.nWtRiKUtqwpVedwyHOifI6d4f-OnhwfNv5RBdUQM2GpIu65YE8ZoU6Da0Hvwvmeyf7ZB8KbQS7yokZ8UuTrN7w", Role.CLIENT)

        CoroutineScope(Dispatchers.Main).launch {

            delay(3000)

            if (!repo.isLoggedIn()) {
                ChooseRoleActivity.navigate(this@SplashScreenActivity)
            } else {
                if (repo.userInfo?.role!!.isClient()) {
                    ClientHomeActivity.navigate(this@SplashScreenActivity)
                } else {
                    MonitorHomeActivity.navigate(this@SplashScreenActivity)
                }
            }
            finish()
        }
    }
}