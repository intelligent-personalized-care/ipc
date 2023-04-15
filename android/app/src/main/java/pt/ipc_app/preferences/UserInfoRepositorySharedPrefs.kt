package pt.ipc_app.preferences

import android.content.Context
import pt.ipc_app.domain.UserInfo
import java.util.Date

class UserInfoSharedPrefs(private val context: Context) {

    private val userNameKey = "Name"

    private val prefs by lazy {
        context.getSharedPreferences("UserInfoPrefs", Context.MODE_PRIVATE)
    }

    var userInfo: UserInfo?
        get() {
            val savedNick = prefs.getString(userNameKey, null)
            return if (savedNick != null)
                UserInfo(savedNick, "", "", "", 0, 0, "")
            else
                null
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(userNameKey)
                    .apply()
            else
                prefs.edit()
                    .putString(userNameKey, value.name)
                    .apply()
        }
}