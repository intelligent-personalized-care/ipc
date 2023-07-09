package pt.ipc_app.ui.screens.plan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.ui.components.bottomBar.ButtonBarType
import pt.ipc_app.ui.setAppContentMonitor
import pt.ipc_app.utils.viewModelInit

/**
 * The create plan activity.
 */
class CreatePlanActivity : ComponentActivity() {

    private val viewModel by viewModels<CreatePlanViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            CreatePlanViewModel(app.services.plansService, app.services.exercisesService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, CreatePlanActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.changeButtonBar(ButtonBarType.PLANS)
        viewModel.getExercises()

        setAppContentMonitor(viewModel) {
            CreatePlanScreen(
                exercises = viewModel.exercises.collectAsState().value,
                onPlanCreation = {
                    viewModel.createPlan(it)
                },
                onExercisesPaginationClick = { viewModel.getExercises(it) }
            )
        }
    }
}