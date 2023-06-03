package pt.ipc_app.ui.screens.plan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 * The create plan activity.
 */
class CreatePlanActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, CreatePlanActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreatePlanScreen(
                exercises = listOf()
            )
        }
    }
}