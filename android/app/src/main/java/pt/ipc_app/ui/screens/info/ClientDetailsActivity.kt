package pt.ipc_app.ui.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.net.toFile
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.getFileNameFromUri
import pt.ipc_app.ui.components.openSendEmail
import pt.ipc_app.utils.viewModelInit
import java.io.File


/**
 * The client details activity.
 */
class ClientDetailsActivity : ComponentActivity() {

    private val viewModel by viewModels<ClientDetailsViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ClientDetailsViewModel(app.services.usersService, app.sessionManager)
        }
    }

    companion object {
        const val CLIENT = "CLIENT"
        fun navigate(context: Context, client: ClientOutput) {
            with(context) {
                val intent = Intent(this, ClientDetailsActivity::class.java)
                intent.putExtra(CLIENT, client)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientDetailsScreen(
                client = client,
                onSendEmailRequest = { openSendEmail(client.email) },
                onUpdateProfilePicture = { imageChooser() }
            )
        }
    }

    // constant to compare
    // the activity result code
    var SELECT_PICTURE = 200

    fun imageChooser() {

        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            Log.d("Picture Path", selectedImageUri?.path.toString())

            val path: File = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            viewModel.updatePicture(File(path, "IMG_20230520_205825.jpg"))
        }
    }


    @Suppress("deprecation")
    private val client: ClientOutput by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(CLIENT, ClientOutput::class.java)
        else
            intent.getParcelableExtra(CLIENT)
        checkNotNull(exe)
    }
}