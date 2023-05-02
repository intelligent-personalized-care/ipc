package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.user.*
import pt.ipc_app.ui.components.plan

/**
 * The client home activity.
 */
class ClientHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, ClientHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientHomeScreen(
                client = repo.userInfo!!,
                monitor = Monitor("Miguel", "miguel@gmail.com", "Aa123456@", null, "Physiotherapist"),
                plan = plan
            )
        }
    }
}