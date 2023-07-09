package pt.ipc_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import pt.ipc_app.ui.components.bottomBar.ButtonBarType
import pt.ipc_app.ui.components.bottomBar.ClientBottomBar
import pt.ipc_app.ui.components.bottomBar.MonitorBottomBar
import pt.ipc_app.ui.screens.about.AboutActivity
import pt.ipc_app.ui.screens.exercises.list.ExercisesListActivity
import pt.ipc_app.ui.screens.home.ClientHomeActivity
import pt.ipc_app.ui.screens.home.MonitorHomeActivity
import pt.ipc_app.ui.screens.plan.CreatePlanActivity
import pt.ipc_app.ui.screens.profile.ClientProfileActivity
import pt.ipc_app.ui.screens.profile.MonitorProfileActivity
import pt.ipc_app.ui.theme.AppTheme
import pt.ipc_app.ui.components.TopBar

/**
 * A screen that displays the app of client.
 *
 * @param content the content to be displayed
 */
@Composable
fun AppClientScreen(
    buttonBarClicked: ButtonBarType = ButtonBarType.HOME,
    content: @Composable () -> Unit
) {
    AppTheme {
        val ctx = LocalContext.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(onInfoRequested = { AboutActivity.navigate(ctx) })
            },
            bottomBar = {
                ClientBottomBar(
                    buttonClicked = buttonBarClicked,
                    onHomeClick = { ClientHomeActivity.navigate(ctx) },
                    onExercisesClick = { ExercisesListActivity.navigate(ctx) },
                    onProfileClick = { ClientProfileActivity.navigate(ctx) }
                )
            },
            content = { content() }
        )
    }
}

/**
 * A screen that displays the app of monitor.
 *
 * @param content the content to be displayed
 */
@Composable
fun AppMonitorScreen(
    buttonBarClicked: ButtonBarType = ButtonBarType.HOME,
    content: @Composable () -> Unit
) {
    AppTheme {
        val ctx = LocalContext.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(onInfoRequested = { AboutActivity.navigate(ctx) })
            },
            bottomBar = {
                MonitorBottomBar(
                    buttonClicked = buttonBarClicked,
                    onHomeClick = { MonitorHomeActivity.navigate(ctx) },
                    onPlanCreateClick = { CreatePlanActivity.navigate(ctx) },
                    onProfileClick = { MonitorProfileActivity.navigate(ctx) }
                )
            },
            content = { content() }
        )
    }
}
