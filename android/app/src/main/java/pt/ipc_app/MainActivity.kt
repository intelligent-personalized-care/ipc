package pt.ipc_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Button
import androidx.compose.material.Text
import pt.ipc_app.utils.viewModelInit

/**
 * The start screen.
 */
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            RegisterViewModel(app.services.usersService, app.sessionManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = { viewModel.register(
                name = "Test1",
                email = "test1@gmail.com",
                password = "Password1@",
                weight = 70,
                height = 184,
                birthDate = "2002-02-03",
                physicalCondition = "lesão na perna esquerda"
            ) }) {
                Text(text = "Create Client")
            }
        }
    }

}