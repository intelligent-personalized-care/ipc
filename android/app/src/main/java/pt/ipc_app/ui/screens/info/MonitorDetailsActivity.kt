package pt.ipc_app.ui.screens.info

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.components.openSendEmail

/**
 * The monitor details activity.
 */
class MonitorDetailsActivity : ComponentActivity() {

    companion object {
        const val MONITOR = "MONITOR"
        fun navigate(context: Context, monitor: MonitorOutput) {
            with(context) {
                val intent = Intent(this, MonitorDetailsActivity::class.java)
                intent.putExtra(MONITOR, monitor)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitorDetailsScreen(
                monitor = monitor,
                onSendEmailRequest = { openSendEmail(monitor.email) }
            )
        }
    }

    @Suppress("deprecation")
    private val monitor: MonitorOutput by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(MONITOR, MonitorOutput::class.java)
        else
            intent.getParcelableExtra(MONITOR)
        checkNotNull(exe)
    }
}