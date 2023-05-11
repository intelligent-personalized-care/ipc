package pt.ipc_app.ui.screens.exercise

import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Login
import androidx.compose.ui.Alignment
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.Exercise
import pt.ipc_app.mlkit.EntryChoiceActivity
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

        setContent {
            ExerciseScreen(
                exercise = exercise,
                onRecordClick = {EntryChoiceActivity.navigate(this)}
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