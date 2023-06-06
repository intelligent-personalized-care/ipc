package pt.ipc_app.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.R
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.components.ClientsTable
import pt.ipc_app.ui.screens.AppScreen
import java.util.UUID

@Composable
fun SearchClientsScreen(
    clients: List<ClientOutput>,
    onClientClick: (ClientOutput) -> Unit = { },
) {
    AppScreen {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.search_results),
                style = MaterialTheme.typography.h4,
            )
            ClientsTable(
                columnText = stringResource(id = R.string.search_clients),
                clients = clients,
                modifier = Modifier.weight(weight = 1f, fill = false)
            )
        }
    }
}

@Preview
@Composable
fun SearchClientsScreenPreview() {
    SearchClientsScreen(
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
