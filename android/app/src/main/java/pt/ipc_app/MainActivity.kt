package pt.ipc_app

import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.isClient
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
        repo.setSession("Tiago", "", Role.CLIENT)
        if (!repo.isLoggedIn())
            ChooseRoleActivity.navigate(this)
        else {
            if (repo.userInfo!!.role.isClient())
                ClientHomeActivity.navigate(this)
            else
                MonitorHomeActivity.navigate(this)
        }
        finish()
    }

}