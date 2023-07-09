package pt.ipc_app.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.R
import pt.ipc_app.ui.theme.AppTheme

const val NavigateToInfoTestTag = "NavigateToInfo"

@Composable
fun TopBar(
    onInfoRequested: () -> Unit = { }
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = onInfoRequested,
                modifier = Modifier.testTag(NavigateToInfoTestTag)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null
                )
            }
        },
        backgroundColor = Color(27, 69, 113),
        contentColor = Color.White
    )}


@Preview
@Composable
private fun TopBarPreview() {
    AppTheme {
        TopBar()
    }
}
