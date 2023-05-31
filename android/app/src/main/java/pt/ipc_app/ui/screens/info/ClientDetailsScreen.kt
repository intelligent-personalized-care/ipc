package pt.ipc_app.ui.screens.info

import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.FilePicker
import pt.ipc_app.ui.components.getFileNameFromUri
import pt.ipc_app.ui.screens.AppScreen
import java.io.File

@Composable
fun ClientDetailsScreen(
    client: ClientOutput,
    onSendEmailRequest: () -> Unit = { },
    onUpdateProfilePicture: () -> Unit = { }
) {
    AppScreen {
        Column {
            Text(client.name)
            Button(onClick = onSendEmailRequest) {
                Text(client.email)
            }

            FilePicker(
                text = "Update Picture",
                fileType = "*/*",
                onChooseFile = {
                    onUpdateProfilePicture()
                }
            )
        }

    }

}

@Preview
@Composable
fun ClientDetailsScreenPreview() {

}