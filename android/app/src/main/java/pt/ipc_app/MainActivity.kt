package pt.ipc_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.domain.user.Role
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.ui.screens.role.ChooseRoleActivity

/**
 * The start screen.
 */
class MainActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (!repo.isLoggedIn())
                ChooseRoleActivity.navigate(this)
            else {
                if (Role.isClient(repo.role!!))
                    ClientHomeActivity.navigate(this)
                else
                    MonitorHomeActivity.navigate(this)
            }
            finish()
        }
    }

}