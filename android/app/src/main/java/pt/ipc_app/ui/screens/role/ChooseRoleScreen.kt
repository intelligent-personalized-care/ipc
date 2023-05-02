package pt.ipc_app.ui.screens.role

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.R
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
                    onClick = { role = if (role != Role.CLIENT) Role.CLIENT else null },
                    colors = styleButtonIf(role == Role.CLIENT)
                ) {
                    Text(stringResource(id = R.string.client))
                }
                Button(
                    onClick = { role = if (role != Role.MONITOR) Role.MONITOR else null },
                    colors = styleButtonIf(role == Role.MONITOR)
                ) {
                    Text(stringResource(id = R.string.monitor))
                }
            }

            Button(
                onClick = { role?.let { onRoleChoose(it) } },
                enabled = role != null
            ) {
                Text(stringResource(id = R.string.select))
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