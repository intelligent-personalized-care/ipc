package pt.ipc_app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream

@Composable
fun FilePicker(
    text: String,
    fileType: String = "*/*",
    onChooseFile: (Uri) -> Unit
) {
    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { pickedFileUri = it }
    )

    pickedFileUri?.let { uri ->
        onChooseFile(uri)
    }

    Button(
        onClick = { launcher.launch(fileType) },
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(text)
    }
}
