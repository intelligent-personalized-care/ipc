package pt.ipc_app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.users.ClientOutput

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientsTable(
    columnText: String,
    clients: List<ClientOutput>,
    modifier: Modifier = Modifier,
    onClientClick: (ClientOutput) -> Unit = { }
) {
    val usernameWeight = 1f

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 50.dp)
            .height(180.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier.background(Color(27, 69, 113))
            ) {
                ClientInfoEntry(
                    text = columnText,
                    textColor = Color.White,
                    weight = usernameWeight
                )
            }
        }

        items(clients) {
            Row(modifier = modifier.fillMaxWidth()) {
                ClientInfoEntry(
                    text = it.name,
                    weight = usernameWeight,
                    textAlign = TextAlign.Left,
                    onPersonClick = { onClientClick(it) },
                    enable = true
                )
            }
        }
    }
}

@Composable
fun RowScope.ClientInfoEntry(
    text: String,
    textColor: Color = Color.Black,
    weight: Float,
    textAlign: TextAlign = TextAlign.Center,
    onPersonClick: () -> Unit = { },
    enable: Boolean = false
) {
    Text(
        text = text,
        color = textColor,
        modifier = Modifier
            .weight(weight)
            .padding(8.dp)
            .clickable(
                enabled = enable,
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onPersonClick
            ),
        textAlign = textAlign
    )
}
