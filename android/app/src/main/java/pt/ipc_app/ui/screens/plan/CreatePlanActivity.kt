package pt.ipc_app.ui.screens.plan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.dailyList.DailyListInput
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

        viewModel.getExercises()

        setContent {
            CreatePlanScreen(
                exercises = viewModel.exercises.collectAsState().value,
                onPlanCreation = { viewModel.createPlan(it) }
            )
        }
    }
}