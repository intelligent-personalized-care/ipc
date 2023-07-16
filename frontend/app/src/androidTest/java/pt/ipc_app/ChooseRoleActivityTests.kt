package pt.ipc_app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pt.ipc_app.ui.screens.login.LoginScreenTag
import pt.ipc_app.ui.screens.register.RegisterClientScreenTag
import pt.ipc_app.ui.screens.register.RegisterMonitorScreenTag
import pt.ipc_app.ui.screens.role.*
import java.util.*

@RunWith(AndroidJUnit4::class)
class ChooseRoleActivityTests {

    @get:Rule
    val testRule = createAndroidComposeRule<ChooseRoleActivity>()

    private val app by lazy {
        InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as IPCApplication
    }

    private val user = if (app.sessionManager.isLoggedIn()) app.sessionManager.userLoggedIn else null

    @Before
    fun clearSession() {
        app.sessionManager.clearSession()
    }

    @After
    fun resetSession() {
        user?.let {
            app.sessionManager.setSession(it.id, it.name, it.accessToken, it.refreshToken, it.role)
        }
    }

    @Test
    fun choosing_client_role_navigates_to_register_client() {

        testRule.onNodeWithTag(ChooseClientButtonTag).performClick()
        testRule.onNodeWithTag(SelectButtonTag).performClick()
        testRule.waitForIdle()

        testRule.onNodeWithTag(RegisterClientScreenTag).assertExists()
    }

    @Test
    fun choosing_monitor_role_navigates_to_register_monitor() {

        testRule.onNodeWithTag(ChooseMonitorButtonTag).performClick()
        testRule.onNodeWithTag(SelectButtonTag).performClick()
        testRule.waitForIdle()

        testRule.onNodeWithTag(RegisterMonitorScreenTag).assertExists()
    }

    @Test
    fun pressing_login_navigates_to_login() {

        testRule.onNodeWithTag(LoginButtonTag).performClick()
        testRule.waitForIdle()

        testRule.onNodeWithTag(LoginScreenTag).assertExists()
    }

}