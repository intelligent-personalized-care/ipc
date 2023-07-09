package pt.ipc_app.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.R
import pt.ipc_app.domain.user.User
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.ui.components.*

/**
 * Login user screen.
 *
 * @param onSaveRequest callback to be invoked when the login button is clicked
 */
@Composable
fun LoginScreen(
    progressState: ProgressState = ProgressState.IDLE,
    onSaveRequest: (email: String, password: String) -> Unit = { _,_ -> }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.login_screen_title),
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CustomTextField(
                fieldType = TextFieldType.EMAIL,
                textToDisplay = email,
                updateText = { email = it },
                maxLength = User.EMAIL_LENGTH_RANGE.last,
                iconImageVector = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )
            CustomTextField(
                fieldType = TextFieldType.PASSWORD,
                textToDisplay = password,
                updateText = { password = it },
                iconImageVector = Icons.Default.Password,
                hide = true,
                keyboardType = KeyboardType.Password,
            )
        }
        CircularButton(
            icon = Icons.Default.Login,
            isEnabled = User.validateEmail(email) && User.validatePassword(password),
            state = progressState,
            onClick = { onSaveRequest(email, password) }
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}