package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.ui.screens.search.SearchClientsActivity

/**
 * The monitor home activity.
 */
class MonitorHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, MonitorHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitorHomeScreen(
                monitor = repo.userInfo!!,
                onClientsRequest = { SearchClientsActivity.navigate(this) }
            )
        }
    }
}