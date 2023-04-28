package pt.ipc_app.ui.screens.role

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.domain.user.Role
import pt.ipc_app.ui.screens.register.RegisterActivity

/**
 * The choose role activity.
 */
class ChooseRoleActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, ChooseRoleActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChooseRoleScreen(
                onRoleChoose = {
                    RegisterActivity.navigate(this, it.name)
                    finish()
                }
            )
        }
    }
}