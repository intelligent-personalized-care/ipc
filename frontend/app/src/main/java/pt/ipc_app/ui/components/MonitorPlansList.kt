package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pt.ipc_app.service.models.plans.PlanInfoOutput

@Composable
fun MonitorPlansList(
    plans: List<PlanInfoOutput>,
    onAssociatePlan: (Int) -> Unit
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
                        onAssociatePlan(it.id)
                    }
                    .padding(8.dp)
            ) {
                Column {
                    Text(it.title)
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Row {
                    Text(
                        text = "${it.days}d",
                        color = Color(20, 129, 202, 255),
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Plan",
                        tint = Color(131, 204, 46, 255)
                    )
                }
            }
        }
    }
}