package pt.ipc_app.ui.screens.exercises.info

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.mlkit.vision.CameraXLivePreviewActivity
import pt.ipc_app.ui.screens.exercises.ExercisesViewModel
import pt.ipc_app.ui.setCustomContent
import pt.ipc_app.utils.viewModelInit

class ExerciseActivity: ComponentActivity() {

    private val viewModel by viewModels<ExercisesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExercisesViewModel(app.services.exercisesService, app.sessionManager)
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCustomContent(viewModel) {
            ExerciseScreen(
                exercise = exercise,
                isToRecord = if (exercise is ExerciseTotalInfo) !(exercise as ExerciseTotalInfo).exercise.isDone else true,
                exercisePreviewUrl = viewModel.getExercisePreviewUrl(exercise.exeID),
                onRecordClick = {
                    finish()
                    CameraXLivePreviewActivity.navigate(this, exercise)
                }
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

}