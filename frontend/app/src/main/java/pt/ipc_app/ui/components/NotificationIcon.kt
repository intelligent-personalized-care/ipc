package pt.ipc_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NotificationIcon(
    notifications: Boolean,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.padding(30.dp)
    ) {
        BadgedBox(modifier = Modifier.padding(end = 10.dp),
            badge = {
                if (notifications)
                    Badge(
                        Modifier
                            .clip(CircleShape)
                            .background(Color.Red)
                            .align(Alignment.BottomEnd)
                    )
            }
        ) {
            Icon(
                imageVector = if (notifications) Icons.Default.Notifications else Icons.Default.NotificationsNone,
                contentDescription = "notification icon",
                tint = Color.Black,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onClick
                )
            )
        }
    }
}