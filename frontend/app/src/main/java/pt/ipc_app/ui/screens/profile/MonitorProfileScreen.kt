package pt.ipc_app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.DocState
import pt.ipc_app.service.models.users.MonitorProfile
import pt.ipc_app.service.models.users.Rating
import pt.ipc_app.ui.components.*
import java.util.*

@Composable
fun MonitorProfileScreen(
    monitor: MonitorProfile?,
    profilePicture: @Composable () -> Unit = { },
    updateProfilePictureState: ProgressState = ProgressState.IDLE,
    onUpdateProfilePicture: () -> Unit = { },
    onSuccessUpdateProfilePicture: () -> Unit = { },
    submitCredentialDocumentState: ProgressState = ProgressState.IDLE,
    onSubmitCredentialDocument: () -> Unit = { },
    onSuccessSubmitCredentialDocument: () -> Unit = { },
    onLogout: () -> Unit = { }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        profilePicture()
        monitor?.let {
            ButtonToUpdatePicture(
                updateProfilePictureState = updateProfilePictureState,
                onUpdateProfilePicture = onUpdateProfilePicture,
                onSuccessUpdateProfilePicture = onSuccessUpdateProfilePicture,
            )
        }

        Text(
            text = monitor?.name ?: "",
            style = MaterialTheme.typography.h6
        )

        TextEmail(
            email = monitor?.email ?: "",
            clickable = false,
            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
        )

        monitor?.let {
            MonitorRating(rating = monitor.rating)

            Spacer(modifier = Modifier.padding(top = 100.dp))

            if (monitor.docState == null)
                Button(
                    onClick = onSubmitCredentialDocument,
                    enabled = submitCredentialDocumentState != ProgressState.WAITING,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text("Submit Credential")
                }
            else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(300.dp)
                        .height(60.dp)
                        .background(Color.White)
                        .border(1.dp, Color(204, 202, 202, 255))
                        .padding(8.dp)
                ) {
                    Column {
                        Text("Credential")
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Row {
                        when (monitor.documentState()) {
                            DocState.VALID -> Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Document State Valid",
                                tint = Color(131, 204, 46, 255)
                            )
                            DocState.WAITING -> Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = "Document State Waiting",
                                tint = Color(255, 217, 102, 255)
                            )
                            else -> Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = "Document State Invalid",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }

            if (submitCredentialDocumentState == ProgressState.WAITING) {
                CircularProgressIndicator()
            } else {
                if (submitCredentialDocumentState == ProgressState.FINISHED)
                    onSuccessSubmitCredentialDocument()
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxHeight().padding(bottom = 50.dp)
            ) {
                CircularButton(
                    icon = Icons.Default.Logout,
                    color = Color.Red,
                    onClick = onLogout
                )
            }
        }
    }
}

@Preview
@Composable
fun MonitorProfileScreenPreview() {
    MonitorProfileScreen(
        monitor = MonitorProfile(UUID.randomUUID(), "Test", "test@gmail.com", Rating(4F, 1), "Valid")
    )
}