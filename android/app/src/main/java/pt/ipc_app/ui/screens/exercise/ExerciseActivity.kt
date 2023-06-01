package pt.ipc_app.ui.screens.exercise

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.Exercise
import pt.ipc_app.mlkit.vision.CameraXLivePreviewActivity
import pt.ipc_app.mlkit.vision.LivePreviewActivity
import pt.ipc_app.utils.viewModelInit

class ExerciseActivity: ComponentActivity() {

    private val viewModel by viewModels<ExerciseViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExerciseViewModel(app.services.exercisesService, app.sessionManager)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExerciseScreen(
                exercise = exercise,
                //onRecordClick = { LivePreviewActivity.navigate(this) }
                onRecordClick = { CameraXLivePreviewActivity.navigate(this, exercise) }
            )

        }
    }

    @Suppress("deprecation")
    private val exercise: Exercise by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXERCISE, Exercise::class.java)
        else
            intent.getParcelableExtra(EXERCISE)
        checkNotNull(exe)
    }

    companion object {
        const val EXERCISE = "EXERCISE"
        fun navigate(context: Context, exercise: Exercise) {
            with(context) {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra(EXERCISE, exercise)
                startActivity(intent)
            }
        }
    }

}