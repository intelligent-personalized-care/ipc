package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextEmail(
    email: String,
    clickable: Boolean = true,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Row(
        modifier = if (clickable) modifier.clickable { onClick() } else modifier
    ) {
        if (clickable)
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
            )
        Text(text = email)
    }
}