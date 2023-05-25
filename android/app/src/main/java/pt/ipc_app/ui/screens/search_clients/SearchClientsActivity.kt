package pt.ipc_app.ui.screens.search_clients

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * The search clients activity.
 */
class SearchClientsActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, SearchClientsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListClientsScreen(
                clients = listOf(),
                onSearchRequest = {  },
                onClientClick = { }
            )
        }
    }
}