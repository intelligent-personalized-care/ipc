package pt.ipc_app.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import pt.ipc_app.R
import pt.ipc_app.domain.user.User
import pt.ipc_app.domain.user.User.Companion.userOrNull
import pt.ipc_app.ui.components.CustomTextField
import kotlin.math.min

@Composable
fun UserRegister(
    userValidation: (User?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    userValidation(userOrNull(name, email, password))

    CustomTextField(
        labelId = R.string.register_screen_label_name,
        textToDisplay = name,
        updateText = { name = it },
        maxLength = User.NAME_LENGTH_RANGE.last,
        isToTrim = false,
        iconImageVector = Icons.Default.Face
    )
    CustomTextField(
        labelId = R.string.register_screen_label_email,
        textToDisplay = email,
        updateText = { email = it },
        maxLength = User.EMAIL_LENGTH_RANGE.last,
        iconImageVector = Icons.Default.Email,
        keyboardType = KeyboardType.Email
    )
    CustomTextField(
        labelId = R.string.register_screen_label_password,
        textToDisplay = password,
        updateText = { password = it },
        iconImageVector = Icons.Default.Password,
        hide = true,
        keyboardType = KeyboardType.Password
    )
}

@Composable
fun ButtonRegister(
    validationInfo: User?,
    onClick: (User) -> Unit = {}
) {
    var editing by remember { mutableStateOf(validationInfo == null) }

    Spacer(modifier = Modifier.height(5.dp))

    Button(
        onClick = {
            if (!editing) editing = true
            else if (validationInfo != null) { onClick(validationInfo) }
        },
        enabled = validationInfo != null,
        shape = CircleShape,
        modifier = Modifier
            .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
    ) {
        Icon(imageVector = Icons.Default.Login, contentDescription = "")
    }
}

/*
@Preview
@Composable
private fun RegisterScreenPreview() {
    UserRegister(
        onSaveRequest = {}
    )
}

 */
