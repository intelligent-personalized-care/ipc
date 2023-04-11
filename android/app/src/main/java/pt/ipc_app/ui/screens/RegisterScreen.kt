package pt.ipc_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.UserInfo

/**
 * Register screen.
 *
 * @param onRegister callback to be invoked when the register button is clicked
 * @param onRegisterSuccessful callback to be invoked when the register process is successful
 * @param onBackButtonClicked callback to be invoked when the back button is clicked
 */
@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit,
    onRegisterSuccessful: () -> Unit,
    onBackButtonClicked: () -> Unit
) {

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var physicalCondition by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }

    val validationInfo = UserInfo.validateUserInfo(username, email, password)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* enviar formulário */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create")
        }

    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        onRegister = { _, _, _ -> },
        onRegisterSuccessful = { },
        onBackButtonClicked = { }
    )
}
