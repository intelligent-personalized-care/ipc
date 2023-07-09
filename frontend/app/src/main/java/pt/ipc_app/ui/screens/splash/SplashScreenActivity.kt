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
            id = "78d82d8d-36a5-4f15-bc71-b846887555fe",
            name = "Tiago",
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiI3OGQ4MmQ4ZC0zNmE1LTRmMTUtYmM3MS1iODQ2ODg3NTU1ZmUiLCJyb2xlIjoiTU9OSVRPUiIsInNlc3Npb25JRCI6ImYxMTZlYTUzLWE4ZDEtNDE3OS05Nzg0LWM1MzM3MDVmNzNhMSIsImV4cCI6MTY4ODk2NTIwNn0.W_Vt7RFqddS_to4a_kpDmV0j5oOJ2KS5AQrx-BFfANf1YbNfuxjHg8LbrpqImLi2S2jU6VJDL5F7IFwxG78LSg",
            refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzZXNzaW9uSUQiOiJmMTE2ZWE1My1hOGQxLTQxNzktOTc4NC1jNTMzNzA1ZjczYTEiLCJleHAiOjE2ODg5NjUyMDZ9.abZ4Jkiw2czDsW0GmF2uT_V6o-hNTGqDfXh1lzayS7FyuPoZ5ELQtQH-MCJPIQ0mptr-DgBURb69deUlCGV88Q",
            role = Role.MONITOR
        )

 */

/*
        repo.setSession(
            id = "a26a52cb-6d7d-4957-91b3-b78e941dd384",
            name = "Tiago",
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySUQiOiJhMjZhNTJjYi02ZDdkLTQ5NTctOTFiMy1iNzhlOTQxZGQzODQiLCJyb2xlIjoiQ0xJRU5UIiwic2Vzc2lvbklEIjoiZmQ1MTdjODEtYzkzOC00ZjY3LTg4NGYtZTdkNzc1OTkxYTM4IiwiZXhwIjoxNjg4OTgwMjY5fQ.y-H-sSfZ_HhuQVUVAUZDoRRAu5ajIQL8SAsiRkMSprTW1eyB2iNKxo3aO6d1mBmmh4jp34S5i4K8rNh0kBIpDw",
            refreshToken = "",
            role = Role.CLIENT
        )

 */
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