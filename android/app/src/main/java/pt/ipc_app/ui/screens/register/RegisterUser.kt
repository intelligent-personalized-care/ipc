package pt.ipc_app.ui.screens.register

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.domain.user.User
import pt.ipc_app.domain.user.User.Companion.userOrNull
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType

@Composable
fun RegisterUser(
    userValidation: (User?) -> Unit,
    error: ProblemJson? = null
    ) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    userValidation(userOrNull(name, email, password))

    CustomTextField(
        fieldType = TextFieldType.NAME,
        textToDisplay = name,
        updateText = { name = it },
        maxLength = User.NAME_LENGTH_RANGE.last,
        isToTrim = false,
        iconImageVector = Icons.Default.Face,
    )
    CustomTextField(
        fieldType = TextFieldType.EMAIL,
        textToDisplay = email,
        updateText = { email = it },
        maxLength = User.EMAIL_LENGTH_RANGE.last,
        iconImageVector = Icons.Default.Email,
        keyboardType = KeyboardType.Email,
        error = error
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

@Preview
@Composable
private fun RegisterUserPreview() {
    RegisterUser(
        userValidation = {}
    )
}
