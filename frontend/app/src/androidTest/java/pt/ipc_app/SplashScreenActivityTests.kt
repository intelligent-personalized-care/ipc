package pt.ipc_app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.ipc_app.domain.user.Role
import pt.ipc_app.ui.screens.home.ClientHomeScreenTag
import pt.ipc_app.ui.screens.home.MonitorHomeScreenTag
import pt.ipc_app.ui.screens.role.ChooseRoleScreenTag
import pt.ipc_app.ui.screens.splash.SplashScreenActivity
import java.util.*

@RunWith(AndroidJUnit4::class)
class SplashScreenActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<SplashScreenActivity>()

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as IPCApplication
    }

    private val user = if (app.sessionManager.isLoggedIn()) app.sessionManager.userLoggedIn else null

    @After
    fun resetSession() {
        user?.let {
            app.sessionManager.setSession(it.id, it.name, it.accessToken, it.refreshToken, it.role)
        }
    }

    @Test
    fun initialize_app_navigates_to_choose_role_if_user_info_does_not_exists() {

        app.sessionManager.clearSession()

        testRule.waitForIdle()

        testRule.onNodeWithTag(ChooseRoleScreenTag).assertExists()
    }

    @Test
    fun initialize_app_navigates_to_client_home_if_user_info_exists_and_is_client() {

        app.sessionManager.setSession(UUID.randomUUID().toString(), "", "", "", Role.CLIENT)

        testRule.waitForIdle()

        testRule.onNodeWithTag(ClientHomeScreenTag).assertExists()
    }

    @Test
    fun initialize_app_navigates_to_monitor_home_if_user_info_exists_and_is_monitor() {

        app.sessionManager.setSession(UUID.randomUUID().toString(), "", "", "", Role.MONITOR)

        testRule.waitForIdle()

        testRule.onNodeWithTag(MonitorHomeScreenTag).assertExists()
    }
}