package pt.ipc_app.ui.screens.search_clients

import android.view.KeyEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.ClientInfo
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen
import pt.ipc_app.ui.theme.DarkBlue

@Composable
fun ListClientsScreen(
    clients: List<ClientInfo>,
    onSearchRequest: (String) -> Unit = { },
    onClientClick: (ClientInfo) -> Unit = { },
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

@Composable
fun SearchClientsScreen(
    clients: List<ClientInfo>,
    onClientClick: (ClientInfo) -> Unit = { },
) {
    AppScreen {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.clients_search_results),
                style = MaterialTheme.typography.h4,
            )
            ClientsTable(
                clients = clients,
                modifier = Modifier.weight(weight = 1f, fill = false)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientsTable(
    clients: List<ClientInfo>,
    modifier: Modifier = Modifier,
    onClientClick: (ClientInfo) -> Unit = { }
) {
    val usernameWeight = 1f

    LazyColumn(
        modifier = modifier
            .fillMaxSize(fraction = .8f)
            .padding(16.dp)
            .size(1.dp)
    ) {
        stickyHeader {
            Row(modifier = Modifier.background(DarkBlue)) {
                ClientInfoEntry(text = stringResource(id = R.string.clients_search_name), weight = usernameWeight)
            }
        }

        items(clients) {
            Row(modifier = modifier.fillMaxWidth()) {
                ClientInfoEntry(text = it.name, weight = usernameWeight, textAlign = TextAlign.Left, { onClientClick(it) }, true)
            }
        }
    }
}

@Composable
fun RowScope.ClientInfoEntry(
    text: String,
    weight: Float,
    textAlign: TextAlign = TextAlign.Center,
    onPersonClick: () -> Unit = { },
    enable: Boolean = false
) {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
            .clickable(enabled = enable) {
                onPersonClick()
            },
        textAlign = textAlign
    )
}

@Preview
@Composable
fun EmptySearchClientsScreenPreview() {
    ListClientsScreen(clients = listOf())
}

@Preview
@Composable
fun SearchClientsScreenPreview() {
    SearchClientsScreen(
        clients = listOf(
            ClientInfo(
                name = "Tiago"
            ),
            ClientInfo(
                name = "Rodrigo"
            ),
            ClientInfo(
                name = "Guilherme"
            )
        )
    )
}
