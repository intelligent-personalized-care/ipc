package pt.ipc_app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.ui.theme.DarkBlue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientsTable(
    clients: List<ClientOutput>,
    modifier: Modifier = Modifier,
    onClientClick: (ClientOutput) -> Unit = { }
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
                ClientInfoEntry(text = stringResource(id = R.string.search_clients), weight = usernameWeight)
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
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
            .clickable(enabled = enable) {
                onPersonClick()
            },
        textAlign = textAlign
    )
}
