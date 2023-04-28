package pt.ipc_app.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pt.ipc_app.R
import pt.ipc_app.domain.user.Monitor
import pt.ipc_app.domain.user.User
import pt.ipc_app.ui.components.ChooseFile
import pt.ipc_app.ui.screens.AppScreen

/**
 * Register monitor screen.
 *
 * @param onSaveRequest callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterMonitorScreen(
    onSaveRequest: (Monitor) -> Unit
) {
    var userInfo: User? by remember { mutableStateOf(null) }

    var credential: ByteArray by remember { mutableStateOf(ByteArray(0)) }

    val monitorValidation = userInfo?.let {
        Monitor.monitorOrNull(it.name, it.email, it.password, credential)
    }

    AppScreen {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.register_monitor_screen_title),
                    style = MaterialTheme.typography.h5,
                    color = Color.Black,
                )
            }
            Column {
                UserRegister(
                    userValidation = { userInfo = it }
                )
                ChooseFile(
                    text = "Select Credential",
                    fileType = "*/*",
                    onChooseFile = { credential = it }
                )
            }
            ButtonRegister(
                validationInfo = monitorValidation,
                onClick = { if (monitorValidation != null) onSaveRequest(monitorValidation) }
            )
        }
    }
}