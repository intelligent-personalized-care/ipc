package pt.ipc_app.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ButtonToUpdatePicture
import pt.ipc_app.ui.components.ClientPlansList
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun ClientProfileScreen(
    client: ClientOutput,
    profilePicture: @Composable () -> Unit = { },
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
            profilePicture()
            ButtonToUpdatePicture(
                updateProfilePictureState = updateProfilePictureState,
                onUpdateProfilePicture = onUpdateProfilePicture,
                onSuccessUpdateProfilePicture = onSuccessUpdateProfilePicture,
            )

            Text(
                text = client.name,
                style = MaterialTheme.typography.h6
            )

            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
                Text(text = client.email)
            }

            Column(
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Text(text = stringResource(id = R.string.birthDate) + ": ${client.birthDate}")
                Text(text = stringResource(id = R.string.weight) + ": ${client.weight}")
                Text(text = stringResource(id = R.string.height) + ": ${client.height}")
                Text(text = stringResource(id = R.string.physicalCondition) + ": ${client.physicalCondition}")
            }

        }
    }
}

@Preview
@Composable
fun ClientProfileScreenPreview() {
    ClientProfileScreen(
        client = ClientOutput(UUID.randomUUID(), "Test", "test@gmail.com", 70, 184, "joelho esquerdo", "2002-02-03")
    )
}