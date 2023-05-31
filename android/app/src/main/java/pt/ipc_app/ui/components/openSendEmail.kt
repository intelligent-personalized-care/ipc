package pt.ipc_app.ui.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import pt.ipc_app.R
import pt.ipc_app.TAG

fun Context.openSendEmail(email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        Log.e(TAG, "Failed to send email", e)
        Toast
            .makeText(
                this,
                R.string.activity_info_no_suitable_app,
                Toast.LENGTH_LONG
            )
            .show()
    }
}