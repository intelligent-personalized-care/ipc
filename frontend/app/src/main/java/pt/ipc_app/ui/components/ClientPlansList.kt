package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.users.PlanOfClient

@Composable
fun ClientPlansList(
    plans: List<PlanOfClient>,
    onPlanClick: (PlanOfClient) -> Unit = { }
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(top = 10.dp, bottom = 60.dp)
            .border(1.dp, Color(204, 202, 202, 255))
    ) {
        items(plans) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
                    .background(Color.White)
                    .clickable {
                        onPlanClick(it)
                    }
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = it.title)
                    Text(
                        text = "${it.startDate} - ${it.endDate}",
                        style = MaterialTheme.typography.overline
                    )
                }
            }
        }
    }
}