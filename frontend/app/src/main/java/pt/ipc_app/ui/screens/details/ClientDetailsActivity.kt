package pt.ipc_app.ui.screens.details

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ProfilePicture
import pt.ipc_app.ui.openSendEmail
import pt.ipc_app.ui.setCustomContent
import pt.ipc_app.utils.viewModelInit
import java.util.*

/**
 * The client details activity.
 */
class ClientDetailsActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<ClientDetailsViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ClientDetailsViewModel(app.services.plansService, app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        const val CLIENT = "CLIENT"
        fun navigate(context: Context, client: ClientOutput) {
            with(context) {
                val intent = Intent(this, ClientDetailsActivity::class.java)
                intent.putExtra(CLIENT, client)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val monitorId = UUID.fromString(repo.userLoggedIn.id)

        viewModel.getClientDetails(client.id)
        viewModel.getMonitorPlans(monitorId)

        setCustomContent(viewModel) {
            val cl = viewModel.client.collectAsState().value
            if (cl != null)
                ClientDetailsScreen(
                    client = cl,
                    profilePicture = { ProfilePicture(imageRequest = viewModel.getProfilePicture(this, cl.id)) },
                    isMyClient = true,
                    onSendEmailRequest = { openSendEmail(client.email) },
                    plans = viewModel.plans.collectAsState().value.plans,
                    onAssociatePlan = { pid, startDate ->
                        viewModel.associatePlanToClient(
                            monitorId = monitorId,
                            clientId = client.id,
                            planId = pid,
                            startDate = startDate
                        )
                    }
                )
        }
    }

    @Suppress("deprecation")
    private val client: ClientOutput by lazy {
        val client = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(CLIENT, ClientOutput::class.java)
        else
            intent.getParcelableExtra(CLIENT)
        checkNotNull(client)
    }
}