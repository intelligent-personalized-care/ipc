package pt.ipc_app.ui.screens.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pt.ipc_app.ui.openSendEmail

/**
 * Activity for the about screen.
 */
class AboutActivity : ComponentActivity() {

    companion object {
        fun navigate(context: Context) {
            with(context) {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AboutScreen(
                onOpenUrl = { openURL(it) },
                onSendEmail = { openSendEmail(it) }
            )
        }
    }

    /**
     * Opens the given [url].
     *
     * @param url the url to be opened
     */
    private fun openURL(url: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, url)
        startActivity(intent)
    }
}
