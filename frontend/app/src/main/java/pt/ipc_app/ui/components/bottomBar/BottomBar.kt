package pt.ipc_app.ui.components.bottomBar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.isClient

@Composable
fun BottomBar(
    role: Role,
    buttonClicked: ButtonBarType,
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onPlanCreateClick: () -> Unit = { },
    onProfileClick: () -> Unit = { }
) {
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(204, 202, 202, 255)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuButton(
                type = ButtonBarType.HOME,
                enable = buttonClicked == ButtonBarType.HOME,
                onClick = { onHomeClick() }
            )
            if (role.isClient())
                MenuButton(
                    type = ButtonBarType.EXERCISES,
                    enable = buttonClicked == ButtonBarType.EXERCISES,
                    onClick = { onExercisesClick() }
                )
            else
                MenuButton(
                    type = ButtonBarType.PLANS,
                    enable = buttonClicked == ButtonBarType.PLANS,
                    onClick = { onPlanCreateClick() }
                )
            MenuButton(
                type = ButtonBarType.PROFILE,
                enable = buttonClicked == ButtonBarType.PROFILE,
                onClick = { onProfileClick() }
            )
        }
    }
}

@Composable
fun ClientBottomBar(
    buttonClicked: ButtonBarType = ButtonBarType.HOME,
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onProfileClick: () -> Unit = { }
) {
    BottomBar(
        role = Role.CLIENT,
        buttonClicked = buttonClicked,
        onHomeClick = onHomeClick,
        onExercisesClick = onExercisesClick,
        onProfileClick = onProfileClick
    )
}

@Composable
fun MonitorBottomBar(
    buttonClicked: ButtonBarType = ButtonBarType.HOME,
    onHomeClick: () -> Unit = { },
    onPlanCreateClick: () -> Unit = { },
    onProfileClick: () -> Unit = { }
) {
    BottomBar(
        role = Role.MONITOR,
        buttonClicked = buttonClicked,
        onHomeClick = onHomeClick,
        onPlanCreateClick = onPlanCreateClick,
        onProfileClick = onProfileClick
    )
}

@Preview
@Composable
fun ClientBottomBarPreview() {
    ClientBottomBar()
}

@Preview
@Composable
fun MonitorBottomBarPreview() {
    MonitorBottomBar()
}
