package pt.ipc_app.ui.screens.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientDetailsScreen(
    client: ClientOutput,
    onSendEmailRequest: () -> Unit = { },
    updateProfilePictureState: ProgressState,
    onUpdateProfilePicture: () -> Unit = { },
    onSuccessUpdateProfilePicture: () -> Unit = { }
) {
    AppScreen {
        Column {
            Text(client.name)
            Button(onClick = onSendEmailRequest) {
                Text(client.email)
            }

            Button(
                onClick = onUpdateProfilePicture,
                enabled = updateProfilePictureState != ProgressState.WAITING,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text("Update Picture")
            }

            if (updateProfilePictureState == ProgressState.WAITING) {
                CircularProgressIndicator()
            } else {
                if (updateProfilePictureState == ProgressState.FINISHED)
                    onSuccessUpdateProfilePicture()
            }
        }

    }

}

@Preview
@Composable
fun ClientDetailsScreenPreview() {

}