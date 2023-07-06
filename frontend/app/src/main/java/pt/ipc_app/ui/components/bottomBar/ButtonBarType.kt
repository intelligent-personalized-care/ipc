package pt.ipc_app.ui.components.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class ButtonBarType(val iconEnabled: ImageVector, val iconDisabled: ImageVector) {
    HOME(Icons.Filled.Home, Icons.Outlined.Home),
    PLANS(Icons.Filled.PostAdd, Icons.Outlined.PostAdd),
    EXERCISES(Icons.Filled.SportsGymnastics, Icons.Outlined.SportsGymnastics),
    PROFILE(Icons.Filled.Face, Icons.Outlined.Face),
    ABOUT(Icons.Filled.Info, Icons.Outlined.Info)
}
