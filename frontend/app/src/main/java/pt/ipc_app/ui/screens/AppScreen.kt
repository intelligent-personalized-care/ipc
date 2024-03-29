package pt.ipc_app.ui.screens

import android.annotation.SuppressLint
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppClientScreen(
    buttonBarClicked: ButtonBarType = ButtonBarType.HOME,
    onNavigated: () -> Unit = { },
    content: @Composable () -> Unit
) {
    AppTheme {
        val ctx = LocalContext.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(
                    onInfoRequested = {
                        if (buttonBarClicked != ButtonBarType.ABOUT)
                            AboutActivity.navigate(ctx)
                    }
                )
            },
            bottomBar = {
                ClientBottomBar(
                    buttonClicked = buttonBarClicked,
                    onHomeClick = {
                        ClientHomeActivity.navigate(ctx)
                        onNavigated()
                    },
                    onExercisesClick = {
                        ExercisesListActivity.navigate(ctx)
                        onNavigated()
                    },
                    onProfileClick = {
                        ClientProfileActivity.navigate(ctx)
                        onNavigated()
                    }
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppMonitorScreen(
    buttonBarClicked: ButtonBarType = ButtonBarType.HOME,
    onNavigated: () -> Unit = { },
    content: @Composable () -> Unit
) {
    AppTheme {
        val ctx = LocalContext.current
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(
                    onInfoRequested = {
                        if (buttonBarClicked != ButtonBarType.ABOUT)
                            AboutActivity.navigate(ctx)
                    }
                )
            },
            bottomBar = {
                MonitorBottomBar(
                    buttonClicked = buttonBarClicked,
                    onHomeClick = {
                        MonitorHomeActivity.navigate(ctx)
                        onNavigated()
                    },
                    onPlanCreateClick = {
                        CreatePlanActivity.navigate(ctx)
                        onNavigated()
                    },
                    onProfileClick = {
                        MonitorProfileActivity.navigate(ctx)
                        onNavigated()
                    }
                )
            },
            content = { content() }
        )
    }
}
