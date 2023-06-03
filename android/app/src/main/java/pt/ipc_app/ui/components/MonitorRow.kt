package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.users.MonitorOutput
import java.util.*

@Composable
fun MonitorRow(
    monitor: MonitorOutput? = null,
    onMonitorClick: () -> Unit = { }
) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(300.dp)
            .height(80.dp)
            .background(Color.White)
            .border(1.dp, Color(204, 202, 202, 255))
            .clickable {
                onMonitorClick()
            }
            .padding(8.dp)
    ) {

        if (monitor != null)
            MonitorInfo(monitor = monitor)
        else {
            Row {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
                Text("Search for monitors to connect...")
            }
        }
    }
}

@Composable
fun MonitorInfo(
    monitor: MonitorOutput
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(monitor.name)
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
fun MonitorRowWithoutMonitorPreview() {
    MonitorRow()
}

@Preview
@Composable
fun MonitorRowPreview() {
    MonitorRow(
        monitor = MonitorOutput(UUID.randomUUID(), "Miguel", "miguel@gmail.com", 4.8F)
    )
}