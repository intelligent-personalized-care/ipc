package pt.ipc_app.ui.screens.search

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.TextFieldType
import pt.ipc_app.ui.screens.AppScreen
import pt.ipc_app.R


@Composable
fun SearchScreen(
    labelId: Int,
    onSearchRequest: (String) -> Unit = { },
) {
    AppScreen {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            var typedUsername by remember { mutableStateOf(value = "") }

            Text(
                text = stringResource(id = labelId),
                style = MaterialTheme.typography.h4,
            )

            CustomTextField(
                fieldType = TextFieldType.SEARCH,
                textToDisplay = typedUsername,
                updateText = { typedUsername = it },
                iconImageVector = Icons.Default.Search
            )

            Button(onClick = { onSearchRequest(typedUsername) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(R.string.search_monitors)
}
