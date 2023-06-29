package pt.ipc_app.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role
import pt.ipc_app.session.UserInfo
import pt.ipc_app.ui.components.ButtonToUpdatePicture
import pt.ipc_app.ui.components.ProfilePicture
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ClientProfileScreen(
    client: UserInfo,
    profilePictureUrl: String,
    updateProfilePictureState: ProgressState = ProgressState.IDLE,
    onUpdateProfilePicture: () -> Unit = { },
    onSuccessUpdateProfilePicture: () -> Unit = { }
) {
    AppScreen {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            ProfilePicture(url = profilePictureUrl)
            ButtonToUpdatePicture(
                updateProfilePictureState = updateProfilePictureState,
                onUpdateProfilePicture = onUpdateProfilePicture,
                onSuccessUpdateProfilePicture = onSuccessUpdateProfilePicture,
            )

            Text(
                text = client.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

    }

}

@Preview
@Composable
fun ClientProfileScreenPreview() {
    ClientProfileScreen(
        client = UserInfo("1", "Client Test", "", Role.CLIENT),
        profilePictureUrl = ""
    )
}