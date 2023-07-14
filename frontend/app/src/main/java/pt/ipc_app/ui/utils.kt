package pt.ipc_app.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.documentfile.provider.DocumentFile
import pt.ipc_app.R
import pt.ipc_app.TAG
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.ui.components.ErrorAlert
import pt.ipc_app.ui.screens.AppClientScreen
import pt.ipc_app.ui.screens.AppMonitorScreen
import pt.ipc_app.ui.screens.AppViewModel
import pt.ipc_app.ui.screens.login.LoginActivity
import pt.ipc_app.ui.theme.AppTheme
import java.io.File
import java.io.FileOutputStream

fun Context.openSendEmail(email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Failed to send email", e)
        Toast
            .makeText(
                this,
                R.string.activity_info_no_suitable_app,
                Toast.LENGTH_LONG
            )
            .show()
    }
}

fun Context.getFileFromUri(imageUri: Uri): File? {
    val documentFile = DocumentFile.fromSingleUri(this, imageUri)
    return documentFile?.let { file ->
        val inputStream = contentResolver.openInputStream(file.uri)
        val outputFile = file.name?.let { File(this.cacheDir, it) }
        val outputStream = FileOutputStream(outputFile)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        outputFile
    }
}

private fun ComponentActivity.setAppContent(
    viewModel: AppViewModel,
    content: @Composable () -> Unit
) {
    setContent {
        content()
        viewModel.error.collectAsState().value?.let {
            if (it is ProblemJson && it.unauthenticatedResponse())
                ErrorAlert(
                    title = it.title,
                    message = "You need to authenticate yourself.",
                    onDismiss = {
                        LoginActivity.navigate(this)
                        finish()
                    }
                )
            else
                ErrorAlert(
                    title = it.title,
                    message = it.message
                )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun ComponentActivity.setAppContentInitial(
    viewModel: AppViewModel,
    content: @Composable () -> Unit
) {
    setAppContent(viewModel) {
        AppTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                content = { content() }
            )
        }
    }
}


fun ComponentActivity.setAppContentClient(
    viewModel: AppViewModel,
    content: @Composable () -> Unit
) {
    setAppContent(viewModel) {
        AppClientScreen(
            buttonBarClicked = viewModel.buttonBarClicked.collectAsState().value,
            content = content
        )
    }
}

fun ComponentActivity.setAppContentMonitor(
    viewModel: AppViewModel,
    content: @Composable () -> Unit
) {
    setAppContent(viewModel) {
        AppMonitorScreen(
            buttonBarClicked = viewModel.buttonBarClicked.collectAsState().value,
            content = content
        )
    }
}
