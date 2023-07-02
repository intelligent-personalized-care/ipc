package pt.ipc_app.ui.screens.exercises.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.exercise.FreeExercise
import pt.ipc_app.ui.screens.exercises.ExercisesViewModel
import pt.ipc_app.ui.screens.exercises.info.ExerciseActivity
import pt.ipc_app.utils.viewModelInit

class ExercisesListActivity: ComponentActivity() {

    private val viewModel by viewModels<ExercisesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExercisesViewModel(app.services.exercisesService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, ExercisesListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getExercises()

        setContent {
            ExercisesListScreen(
                exercises = viewModel.exercises.collectAsState().value,
                onExerciseClick = { ExerciseActivity.navigate(this, it) },
                onPaginationClick = { viewModel.getExercises(it) }
            )
        }
    }

}