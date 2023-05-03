package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.domain.user.Monitor

@Composable
fun MonitorScreen(
    monitor: Monitor,
    onMonitorSelect: () -> Unit = { }
) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(300.dp)
            .height(80.dp)
            .background(Color.White)
            .border(1.dp, Color(204, 202, 202, 255))
            .clickable {
                onMonitorSelect()
            }
            .padding(8.dp)
    ) {

        Text(monitor.name)
        monitor.title?.let {
            Text(
                text = it,
                fontSize = 13.sp,
                style = MaterialTheme.typography.overline
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(255, 217, 102, 255)
            )
            Text(
                text = monitor.stars.toString() + " stars",
                style = MaterialTheme.typography.overline,
            )
        }

    }

}

@Preview
@Composable
fun MonitorScreenPreview() {
    MonitorScreen(
        monitor = Monitor("Miguel", "miguel@gmail.com", "Aa123456@", null, "Physiotherapist")
    )
}