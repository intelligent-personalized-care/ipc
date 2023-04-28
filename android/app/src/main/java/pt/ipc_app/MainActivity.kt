package pt.ipc_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
            if (!repo.isLoggedIn()) {
                ChooseRoleActivity.navigate(this)
                finish()
            }
        }
    }

}