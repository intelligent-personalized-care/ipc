package pt.ipc_app.session

import android.content.Context
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.toRole
import pt.ipc_app.preferences.UserInfo

/**
 * Session manager that uses shared preferences to store the session.
 *
 * @param context the application context
 */
class SessionManagerSharedPrefs(private val context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE)
    }

    var userInfo: UserInfo?
        get() {
            val savedName = prefs.getString(NAME, null)
            val savedToken = prefs.getString(TOKEN, null)
            val savedRole = prefs.getString(ROLE, null)

            return getUserInfo(savedName, savedToken, savedRole)
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(NAME)
                    .remove(TOKEN)
                    .remove(ROLE)
                    .apply()
            else
                prefs.edit()
                    .putString(NAME, value.name)
                    .putString(TOKEN, value.token)
                    .putString(ROLE, value.role.name)
                    .apply()
        }

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = userInfo != null

    /**
     * Updates the session with the given tokens and username.
     *
     * @param name the user's name
     * @param token the user's token
     * @param role the user's role
     */
    fun setSession(
        name: String,
        token: String,
        role: Role
    ) {
        userInfo = UserInfo(name, token, role)
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        userInfo = null
    }

    private fun getUserInfo(name: String?, token: String?, role: String?): UserInfo? {
        return if (name != null && token != null && role != null) {
            val roleValidation = role.toRole() ?: return null
            UserInfo(
                name = name,
                token = token,
                role = roleValidation
            )
        } else null
    }

    companion object {
        private const val SESSION_PREFS = "session"
        private const val TOKEN = "token"
        private const val NAME = "name"
        private const val ROLE = "role"
    }
}
