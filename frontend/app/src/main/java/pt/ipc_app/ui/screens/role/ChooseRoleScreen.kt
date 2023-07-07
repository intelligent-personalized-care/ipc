package pt.ipc_app.ui.screens.role

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.Role

@Composable
fun ChooseRoleScreen(
    role: Role? = null,
    onRoleChoose: (Role?) -> Unit = { },
    onRoleSelect: (Role) -> Unit = { },
    onLoginClick: () -> Unit = { }
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.choose_role_screen_title),
                style = MaterialTheme.typography.h4,
                color = Color.Black,
            )
        }
        Row {
            Button(
                onClick = { onRoleChoose(if (role != Role.CLIENT) Role.CLIENT else null) },
                colors = styleButtonIf(role == Role.CLIENT),
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.role_client),
                        contentDescription = "Role Client"
                    )
                    Text(
                        text = stringResource(id = R.string.client),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = { onRoleChoose(if (role != Role.MONITOR) Role.MONITOR else null) },
                colors = styleButtonIf(role == Role.MONITOR),
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.role_monitor),
                        contentDescription = "Role Monitor"
                    )
                    Text(
                        text = stringResource(id = R.string.monitor),
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { role?.let { onRoleSelect(it) } },
                enabled = role != null
            ) {
                Text(stringResource(id = R.string.select))
            }

            Text(
                text = stringResource(R.string.already_have_account),
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onLoginClick
                )
            )
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
    ChooseRoleScreen()
}