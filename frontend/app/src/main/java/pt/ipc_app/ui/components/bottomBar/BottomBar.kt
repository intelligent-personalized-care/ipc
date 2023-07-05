package pt.ipc_app.ui.components.bottomBar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
    onProfileClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
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
                icon = if (buttonClicked == ButtonBarType.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                color = buttonColor(buttonClicked, ButtonBarType.HOME),
                onClick = { if (buttonClicked != ButtonBarType.HOME) onHomeClick() }
            )
            if (role.isClient())
                MenuButton(
                    icon = if (buttonClicked == ButtonBarType.EXERCISES) Icons.Filled.SportsGymnastics else Icons.Outlined.SportsGymnastics,
                    color = buttonColor(buttonClicked, ButtonBarType.EXERCISES),
                    onClick = { if (buttonClicked != ButtonBarType.EXERCISES) onExercisesClick() }
                )
            else
                MenuButton(
                    icon = if (buttonClicked == ButtonBarType.PLANS) Icons.Filled.PostAdd else Icons.Outlined.PostAdd,
                    color = buttonColor(buttonClicked, ButtonBarType.PLANS),
                    onClick = { if (buttonClicked != ButtonBarType.PLANS) onPlanCreateClick() }
                )
            MenuButton(
                icon = if (buttonClicked == ButtonBarType.PROFILE) Icons.Filled.Face else Icons.Outlined.Face,
                color = buttonColor(buttonClicked, ButtonBarType.PROFILE),
                onClick = { if (buttonClicked != ButtonBarType.PROFILE) onProfileClick() }
            )
            MenuButton(
                icon = if (buttonClicked == ButtonBarType.ABOUT) Icons.Filled.Info else Icons.Outlined.Info,
                color = buttonColor(buttonClicked, ButtonBarType.ABOUT),
                onClick = { if (buttonClicked != ButtonBarType.ABOUT) onAboutClick() }
            )
        }
    }
}

@Composable
fun ClientBottomBar(
    buttonClicked: ButtonBarType = ButtonBarType.HOME,
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    BottomBar(
        role = Role.CLIENT,
        buttonClicked = buttonClicked,
        onHomeClick = onHomeClick,
        onExercisesClick = onExercisesClick,
        onProfileClick = onProfileClick,
        onAboutClick = onAboutClick
    )
}

@Composable
fun MonitorBottomBar(
    buttonClicked: ButtonBarType = ButtonBarType.HOME,
    onHomeClick: () -> Unit = { },
    onPlanCreateClick: () -> Unit = { },
    onProfileClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    BottomBar(
        role = Role.MONITOR,
        buttonClicked = buttonClicked,
        onHomeClick = onHomeClick,
        onPlanCreateClick = onPlanCreateClick,
        onProfileClick = onProfileClick,
        onAboutClick = onAboutClick
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
