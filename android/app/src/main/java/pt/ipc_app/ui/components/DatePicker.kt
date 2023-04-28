package pt.ipc_app.utils

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import pt.ipc_app.ui.components.CustomTextField
import java.util.*

@Composable
fun DatePicker(
    onDateSelected: (string: String) -> Unit
): DatePickerDialog {

    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mYear-${(mMonth+1).toString().padStart(2, '0')}-${mDayOfMonth.toString().padStart(2, '0')}"
            onDateSelected(mDate.value)
        }, mYear, mMonth, mDay
    )
    return mDatePickerDialog
}

@Preview
@Composable
fun DatePickerPreview() {
    DatePicker { }
}

@Composable
fun MyDatePicker(
    labelId: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
) {
    Box {
        CustomTextField(
            textToDisplay = value,
            readOnly = true,
            labelId = labelId,
            iconImageVector = Icons.Default.DateRange,
            updateText = onValueChange,
        )
        Box(modifier = Modifier
            .matchParentSize()
            .alpha(0f)
            .clickable(onClick = onClick)
        )
    }
}