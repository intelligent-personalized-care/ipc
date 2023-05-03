package pt.ipc_app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = { },
    onExercisesClick: () -> Unit = { },
    onUserInfoClick: () -> Unit = { },
    onAboutClick: () -> Unit = { }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(204, 202, 202, 255)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MenuButton(
            icon = Icons.Default.Home,
            onClick = onHomeClick
        )
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

@Composable
fun MenuButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.size(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        selected = true
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color(46, 180, 214, 255) else Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar()
}