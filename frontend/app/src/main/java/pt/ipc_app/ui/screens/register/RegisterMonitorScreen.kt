package pt.ipc_app.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import pt.ipc_app.R
import pt.ipc_app.domain.user.Monitor
import pt.ipc_app.domain.user.User
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.components.CircularButton
import pt.ipc_app.ui.components.RegisterUser

const val RegisterMonitorScreenTag = "RegisterMonitorScreen"

/**
 * Register monitor screen.
 *
 * @param onSaveRequest callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterMonitorScreen(
    progressState: ProgressState = ProgressState.IDLE,
    error: ProblemJson? = null,
    onSaveRequest: (Monitor) -> Unit
) {
    var userInfo: User? by remember { mutableStateOf(null) }

    val monitorValidation = userInfo?.let {
        Monitor.monitorOrNull(it.name, it.email, it.password)
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(RegisterMonitorScreenTag)
            .fillMaxSize()
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.register_monitor_screen_title),
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterUser(
                userValidation = { userInfo = it },
                error = error
            )
        }
        CircularButton(
            icon = Icons.Default.Login,
            isEnabled = monitorValidation != null,
            state = progressState,
            onClick = { if (monitorValidation != null) onSaveRequest(monitorValidation) }
        )
    }
}