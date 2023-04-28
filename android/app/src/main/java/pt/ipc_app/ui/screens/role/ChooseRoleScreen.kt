package pt.ipc_app.ui.screens.role

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.domain.user.Role
import pt.ipc_app.ui.screens.AppScreen

@Composable
fun ChooseRoleScreen(
    onRoleChoose: (Role) -> Unit
) {
    var role: Role? by remember { mutableStateOf(null) }

    AppScreen {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Button(
                    onClick = { role = Role.CLIENT },
                    colors = styleButtonIf(role == Role.CLIENT)
                ) {
                    Text("Client")
                }
                Button(
                    onClick = { role = Role.MONITOR },
                    colors = styleButtonIf(role == Role.MONITOR)
                ) {
                    Text("Monitor")
                }
            }

            Button(
                onClick = { role?.let { onRoleChoose(it) } },
                enabled = role != null
            ) {
                Text("Select")
            }
        }
    }
}

@Composable
private fun styleButtonIf(cond: Boolean): ButtonColors =
    ButtonDefaults.buttonColors(
        backgroundColor = if (cond) Color.Black else Color.Transparent,
        contentColor = if (cond) Color.White else Color.Black
    )

@Preview
@Composable
fun ChooseRoleScreenPreview() {
    ChooseRoleScreen {}
}