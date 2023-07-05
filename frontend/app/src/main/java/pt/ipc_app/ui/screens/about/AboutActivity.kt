package pt.ipc_app.ui.screens.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.user.isClient
import pt.ipc_app.ui.components.bottomBar.ButtonBarType
import pt.ipc_app.ui.openSendEmail
import pt.ipc_app.ui.screens.AppViewModel
import pt.ipc_app.ui.setAppContentClient
import pt.ipc_app.ui.setAppContentMonitor
import pt.ipc_app.utils.viewModelInit

/**
 * Activity for the about screen.
 */
class AboutActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<AppViewModel> {
        viewModelInit {
            AppViewModel()
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.changeButtonBar(ButtonBarType.ABOUT)

        val content: @Composable () -> Unit = {
            AboutScreen(
                onOpenUrl = { openURL(it) },
                onSendEmail = { openSendEmail(it) }
            )
        }

        if (repo.userLoggedIn.role.isClient())
            setAppContentClient(
                viewModel = viewModel,
                content = content
            )
        else
            setAppContentMonitor(
                viewModel = viewModel,
                content = content
            )
    }

    /**
     * Opens the given [url].
     *
     * @param url the url to be opened
     */
    private fun openURL(url: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, url)
        startActivity(intent)
    }
}
