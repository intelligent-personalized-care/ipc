package pt.ipc_app.ui.screens.exercise

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.exercise.DailyExercise
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.mlkit.vision.CameraXLivePreviewActivity
import pt.ipc_app.utils.viewModelInit

class ExerciseActivity: ComponentActivity() {

    private val viewModel by viewModels<ExerciseViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExerciseViewModel(app.services.exercisesService, app.sessionManager)
        }
    }

    companion object {
        const val EXERCISE = "EXERCISE"
        fun navigate(context: Context, exercise: ExerciseTotalInfo) {
            with(context) {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra(EXERCISE, exercise)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExerciseScreen(
                exercise = exercise.exercise,
                exercisePreviewUrl = viewModel.getExercisePreviewUrl(exercise.exercise.exerciseInfoID),
                onRecordClick = {
                    finish()
                    CameraXLivePreviewActivity.navigate(this, exercise)
                }
            )
        }
    }

    @Suppress("deprecation")
    private val exercise: ExerciseTotalInfo by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXERCISE, ExerciseTotalInfo::class.java)
        else
            intent.getParcelableExtra(EXERCISE)
        checkNotNull(exe)
    }

}