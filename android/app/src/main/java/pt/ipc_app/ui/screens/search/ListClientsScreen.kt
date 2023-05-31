package pt.ipc_app.ui.screens.search

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ClientsTable
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen
import java.util.*

@Composable
fun ListClientsScreen(
    clients: List<ClientOutput>,
    onSearchRequest: (String) -> Unit = { },
    onClientClick: (ClientOutput) -> Unit = { },
) {
    AppScreen {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            var typedUsername by remember { mutableStateOf(value = "") }

            CustomTextField(
                fieldType = TextFieldType.SEARCH,
                textToDisplay = typedUsername,
                updateText = { typedUsername = it },
                iconImageVector = Icons.Filled.Search,
                modifier = Modifier.onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        onSearchRequest(typedUsername)
                        true
                    } else false
                }
            )
            Text(
                text = "Clients",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = 10.dp, bottom = 0.dp)
            )
            ClientsTable(
                clients = clients,
                modifier = Modifier.weight(weight = 1f, fill = false),
                onClientClick = onClientClick
            )
        }
    }
}

@Preview
@Composable
fun ListClientsScreenPreview() {
    ListClientsScreen(
        clients = listOf(
            ClientOutput(
                id = UUID.randomUUID(),
                name = "Tiago",
                email = ""
            ),
            ClientOutput(
                id = UUID.randomUUID(),
                name = "Rodrigo",
                email = ""
            ),
            ClientOutput(
                id = UUID.randomUUID(),
                name = "Guilherme",
                email = ""
            )
        )
    )
}
