package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ButtonToUpdatePicture(
    updateProfilePictureState: ProgressState = ProgressState.IDLE,
    onUpdateProfilePicture: () -> Unit = { },
    onSuccessUpdateProfilePicture: () -> Unit = { }
) {
    Icon(
        imageVector = Icons.Default.Edit,
        contentDescription = "change picture icon",
        tint = Color.Black,
        modifier = Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            enabled = updateProfilePictureState != ProgressState.WAITING,
            onClick = onUpdateProfilePicture
        )
    )

    if (updateProfilePictureState == ProgressState.WAITING) {
        CircularProgressIndicator()
    } else {
        if (updateProfilePictureState == ProgressState.FINISHED)
            onSuccessUpdateProfilePicture()
    }
}