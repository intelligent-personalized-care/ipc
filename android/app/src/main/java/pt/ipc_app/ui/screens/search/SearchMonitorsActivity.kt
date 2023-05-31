package pt.ipc_app.ui.screens.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.utils.viewModelInit

/**
 * The search monitors activity.
 */
class SearchMonitorsActivity : ComponentActivity() {

    private val viewModel by viewModels<SearchClientsViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            SearchClientsViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, SearchMonitorsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListClientsScreen(
                clients = listOf(),
                onSearchRequest = {
                    viewModel.searchMonitors(it)
                },
                onClientClick = { }
            )
        }
    }
}