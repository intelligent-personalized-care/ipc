package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Composable
fun DaysOfWeekRow(
    centerDay: LocalDate = LocalDate.now(),
    daySelected: LocalDate,
    onDaySelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek(centerDay).forEach {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(56.dp)
                    .border(1.dp, Color(131, 129, 129, 255), RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when (it) {
                            LocalDate.now() -> Color(170, 233, 98, 255)
                            daySelected -> Color(174, 237, 245, 255)
                            else -> Color(231, 231, 231, 255)
                        }
                    )
                    .clickable { onDaySelected(it) }
            ) {
                Text(it.dayOfMonth.toString())
                Text(
                    text = it.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun DaysOfWeekRowWithoutLocalDate(
    daySelected: Int,
    totalDays: Int,
    onDaySelected: (Int) -> Unit,
    onDayAdded: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxWidth()
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

@Composable
fun BoxDay(
    day: Int = -1,
    daySelected: Int = -1,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .size(56.dp)
            .border(1.dp, Color(131, 129, 129, 255), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                when {
                    day != -1 && day == daySelected -> Color(174, 237, 245, 255)
                    else -> Color(231, 231, 231, 255)
                }
            )
            .clickable { onClick(day) }
    ) {
        if (day != -1)
            Text("Day ${day + 1}")
        else
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Day"
            )
    }
}

fun daysOfWeek(
    centerDay: LocalDate
): List<LocalDate> =
    listOf(
        centerDay.minusDays(2),
        centerDay.minusDays(1),
        centerDay,
        centerDay.plusDays(1),
        centerDay.plusDays(2),
    )