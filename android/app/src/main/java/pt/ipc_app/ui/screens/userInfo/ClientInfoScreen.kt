package pt.ipc_app.ui.screens.userInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.session.UserInfo
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientInfoScreen(
    client: UserInfo,
    updateProfilePictureState: ProgressState,
    onUpdateProfilePicture: () -> Unit = { },
    onSuccessUpdateProfilePicture: () -> Unit = { }
) {
    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(R.string.userInfo_title),
                style = MaterialTheme.typography.h4
            )
        }
        Column(
            modifier = Modifier.padding(start = 40.dp, top = 100.dp)
        ) {
            Text(client.name)

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
fun ClientInfoScreenPreview() {

}