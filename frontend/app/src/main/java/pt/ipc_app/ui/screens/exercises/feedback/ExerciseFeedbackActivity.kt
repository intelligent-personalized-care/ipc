package pt.ipc_app.ui.screens.exercises.feedback

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.ui.screens.exercises.ExercisesViewModel
import pt.ipc_app.ui.setAppContentMonitor
import pt.ipc_app.utils.viewModelInit
import java.util.*

class ExerciseFeedbackActivity: ComponentActivity() {

    private val viewModel by viewModels<ExercisesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExercisesViewModel(app.services.exercisesService, app.sessionManager)
        }
    }

    companion object {
        const val EXERCISE_TOTAL_INFO = "EXERCISE_TOTAL_INFO"
        const val CLIENT_ID = "CLIENT_ID_OF_EXERCISE"
        fun navigate(context: Context, exercise: ExerciseTotalInfo, clientId: UUID) {
            with(context) {
                val intent = Intent(this, ExerciseFeedbackActivity::class.java)
                intent.putExtra(EXERCISE_TOTAL_INFO, exercise)
                intent.putExtra(CLIENT_ID, clientId.toString())
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContentMonitor(viewModel) {
            ExerciseFeedbackScreen(
                exercise = exercise,
                exercisePreviewUrl =
                viewModel.getExerciseVideoOfClientUrl(
                    clientId = clientId,
                    planId = exercise.planId,
                    dailyListId = exercise.dailyListId,
                    exerciseId = exercise.exercise.id
                )
            )
        }

    }

    @Suppress("deprecation")
    private val exercise: ExerciseTotalInfo by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXERCISE_TOTAL_INFO, ExerciseTotalInfo::class.java)
        else
            intent.getParcelableExtra(EXERCISE_TOTAL_INFO)
        checkNotNull(exe)
    }

    @Suppress("deprecation")
    private val clientId: String by lazy {
        val cId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getStringExtra(CLIENT_ID)
        else
            intent.getStringExtra(CLIENT_ID)
        checkNotNull(cId)
    }

}