package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text

/**
 * The monitor home activity.
 */
class MonitorHomeActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, MonitorHomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello World")
        }
    }
}