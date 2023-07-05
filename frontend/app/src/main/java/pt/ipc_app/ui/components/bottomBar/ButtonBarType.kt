package pt.ipc_app.ui.components.bottomBar

import androidx.compose.ui.graphics.Color

enum class ButtonBarType {
    HOME,
    PLANS,
    EXERCISES,
    PROFILE,
    ABOUT
}

fun buttonColor(buttonClicked: ButtonBarType, button: ButtonBarType): Color =
    if (buttonClicked == button) Color(27, 69, 113) else Color.LightGray
