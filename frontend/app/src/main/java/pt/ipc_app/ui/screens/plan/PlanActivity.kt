package pt.ipc_app.ui.screens.plan

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.ui.screens.exercises.done.ClientExerciseActivity
import pt.ipc_app.ui.setAppContentMonitor
import pt.ipc_app.utils.viewModelInit
import java.util.*

/**
 * The plan activity.
 */
class PlanActivity : ComponentActivity() {

    private val viewModel by viewModels<PlanViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            PlanViewModel(app.services.plansService, app.sessionManager)
        }
    }

    companion object {
        const val CLIENT_ID = "CLIENT_ID"
        const val CLIENT_NAME = "CLIENT_NAME"
        const val PLAN_DATE = "PLAN_DATE"
        fun navigate(context: Context, clientId: UUID, clientName: String, planDate: String) {
            with(context) {
                val intent = Intent(this, PlanActivity::class.java)
                intent.putExtra(CLIENT_ID, clientId.toString())
                intent.putExtra(CLIENT_NAME, clientName)
                intent.putExtra(PLAN_DATE, planDate)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPlanOfClient(clientId, planDate)

        setAppContentMonitor(viewModel) {
            PlanScreen(
                plan = viewModel.plan.collectAsState().value,
                clientName = clientName,
                onExerciseSelect = {
                    if (it.exercise.isDone)
                        ClientExerciseActivity.navigate(this, it, UUID.fromString(clientId))
                    else
                        Toast.makeText(this, "Wait for the exercise to be recorded by client", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    @Suppress("deprecation")
    private val clientId: String by lazy {
        val cId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getStringExtra(CLIENT_ID)
        else
            intent.getStringExtra(CLIENT_ID)
        checkNotNull(cId)
    }

    @Suppress("deprecation")
    private val clientName: String by lazy {
        val cName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getStringExtra(CLIENT_NAME)
        else
            intent.getStringExtra(CLIENT_NAME)
        checkNotNull(cName)
    }

    @Suppress("deprecation")
    private val planDate: String by lazy {
        val pDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getStringExtra(PLAN_DATE)
        else
            intent.getStringExtra(PLAN_DATE)
        checkNotNull(pDate)
    }
}