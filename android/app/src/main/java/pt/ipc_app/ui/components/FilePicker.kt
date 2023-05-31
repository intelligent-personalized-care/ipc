package pt.ipc_app.ui.components

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile

@Composable
fun FilePicker(
    text: String,
    fileType: String = "*/*",
    onChooseFile: () -> Unit
) {
    var pickedFileUri by remember { mutableStateOf<Uri?>(null) }
/*
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { pickedFileUri = it }
    )

    pickedFileUri?.let {

        val documentFile = DocumentFile.fromSingleUri(LocalContext.current, it)

        onChooseFile()
    }

 */

    Button(
        onClick = { onChooseFile() },
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(text)
    }

    pickedFileUri?.let {
        Text(getFileNameFromUri(LocalContext.current.contentResolver, it))
    }
}

fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String {
    var fileName = ""
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameColumnIndex: Int =
                it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameColumnIndex != -1) {
                fileName = it.getString(displayNameColumnIndex)
            }
        }
    }
    return fileName
}
