package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RateMonitor(
    modifier: Modifier = Modifier,
    onSubmitRating: (Int) -> Unit = { }
) {
    var stars by remember { mutableStateOf(-1) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row {
            repeat(5) {
                Icon(
                    imageVector = if (it <= stars) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Star",
                    tint = Color(255, 217, 102, 255),
                    modifier = Modifier.size(50.dp).clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { stars = it }
                    )
                )
            }
        }
        Button(onClick = { onSubmitRating(stars + 1) }) {
            Text(text = "Submit")
        }
    }

}

@Preview
@Composable
fun RateMonitorPreview() {
    RateMonitor()
}