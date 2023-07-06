package pt.ipc_app.ui.components.bottomBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuButton(
    type: ButtonBarType,
    enable: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        if (!enable) onClick()
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val color = if (enable) Color(27, 69, 113) else Color.LightGray
            Icon(
                imageVector = if (enable) type.iconEnabled else type.iconDisabled,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                color = color,
                fontSize = 10.sp
            )
        }
    }
}