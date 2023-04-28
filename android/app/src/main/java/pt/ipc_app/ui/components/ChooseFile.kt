package pt.ipc_app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
fun ChooseFile(
    text: String,
    fileType: String = "*/*",
    onChooseFile: (ByteArray) -> Unit
) {
    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { pickedFileUri = it }
    )

    pickedFileUri?.let { uri ->
        Text(uri.toString())
        
        val contentResolver = LocalContext.current.contentResolver
        val byteArray = ByteArrayOutputStream().use { output ->
            contentResolver.openInputStream(uri)?.use { input ->
                input.copyTo(output)
            }
            output.toByteArray()
        }
        onChooseFile(byteArray)
    }

    Button(
        onClick = { launcher.launch(fileType) }
    ) {
        Text(text)
    }
}
