package pt.ipc_app.ui.components.bottomBar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ClientBottomBar(
    modifier: Modifier = Modifier,
    onExercisesClick: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, Color(204, 202, 202, 255)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MenuButton(
                icon = Icons.Default.SportsGymnastics,
                onClick = onExercisesClick
            )
            MenuButton(
                icon = Icons.Default.Face,
                onClick = onUserInfoClick
            )
            MenuButton(
                icon = Icons.Default.Info,
                onClick = onAboutClick
            )
        }
    }
}

@Preview
@Composable
fun ClientBottomBarPreview() {
    ClientBottomBar()
}