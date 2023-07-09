package pt.ipc_app.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.documentfile.provider.DocumentFile
import pt.ipc_app.R
import pt.ipc_app.TAG
import pt.ipc_app.ui.components.CheckProblemJson
import pt.ipc_app.ui.screens.AppClientScreen
import pt.ipc_app.ui.screens.AppMonitorScreen
import pt.ipc_app.ui.screens.AppViewModel
import pt.ipc_app.ui.screens.login.LoginActivity
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
        viewModel.error?.let {
            if (it.title == "Unauthenticated")
                CheckProblemJson(
                    error = it,
                    message = "You need to authenticate yourself.",
                    onDismiss = {
                        LoginActivity.navigate(this)
                        finish()
                    }
                )
            else
                CheckProblemJson(error = it)
        }
    }
}

fun ComponentActivity.setAppContentClient(
    viewModel: AppViewModel,
    content: @Composable () -> Unit
) {
    setAppContent(viewModel) {
        AppClientScreen(
            buttonBarClicked = viewModel.buttonBarClicked,
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
            buttonBarClicked = viewModel.buttonBarClicked,
            content = content
        )
    }
}

fun Context.createNotification() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "download_channel",
            "File download",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
