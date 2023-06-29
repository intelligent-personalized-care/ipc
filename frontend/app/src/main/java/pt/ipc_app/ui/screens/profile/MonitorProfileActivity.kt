package pt.ipc_app.ui.screens.profile

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.ui.getFileFromUri
import pt.ipc_app.utils.viewModelInit
import java.io.IOException


/**
 * The monitor profile activity.
 */
class MonitorProfileActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    private val viewModel by viewModels<MonitorProfileViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            MonitorProfileViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, MonitorProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitorProfileScreen(
                monitor = repo.userLoggedIn,
                profilePictureUrl = viewModel.getProfilePictureUrl(),
                updateProfilePictureState = viewModel.pictureState.collectAsState().value,
                submitCredentialDocumentState = viewModel.documentState.collectAsState().value,
                onSubmitCredentialDocument = { checkReadStoragePermission() },
                onSuccessSubmitCredentialDocument = { Toast.makeText(this, "Document submitted!", Toast.LENGTH_SHORT).show() }
            )
        }
    }

    private fun checkReadStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestReadStoragePermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        else
            openFileManager()
    }

    private fun openFileManager() {
        fileChooserLauncher.launch("application/pdf")
    }

    private val requestReadStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                openFileManager()
        }

    private val fileChooserLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            try {
                val file = getFileFromUri(uri) ?: throw IllegalArgumentException()

                viewModel.submitCredentialDocument(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}