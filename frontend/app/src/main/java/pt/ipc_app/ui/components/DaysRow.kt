package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DaysRow(
    daySelected: Int,
    totalDays: Int,
    onDaySelected: (Int) -> Unit,
    onDayAdded: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(totalDays) {
            BoxDay(
                day = it,
                daySelected = daySelected,
                onClick = onDaySelected,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        item {
            BoxDay(
                onClick = { onDayAdded() }
            )
        }
    }
}
