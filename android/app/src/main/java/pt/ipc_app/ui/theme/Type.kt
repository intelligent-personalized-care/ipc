package pt.ipc_app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import pt.ipc_app.R


// https://www.fontspace.com/monday-feelings-font-f88501
private val AppFont = FontFamily(
    Font(R.font.nunitoregular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        fontSize = 42.sp
    ),

    button = TextStyle(
        fontFamily = AppFont,
        fontWeight = FontWeight.Normal,
        letterSpacing = 2.sp,
        fontSize = 24.sp,
    )
)
