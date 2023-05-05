package pt.ipc_app.ui.screens.role

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.ui.screens.register.RegisterActivity
import pt.ipc_app.ui.screens.register.RegisterViewModel
import pt.ipc_app.utils.viewModelInit

/**
 * The choose role activity.
 */
class ChooseRoleActivity : ComponentActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            RegisterViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, ChooseRoleActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val roleChose by viewModel.chosenRole.collectAsState()

            ChooseRoleScreen(
                role = roleChose,
                onRoleChoose = { viewModel.selectRole(it) },
                onRoleSelect = { RegisterActivity.navigate(this, it.name) }
            )
        }
    }
}