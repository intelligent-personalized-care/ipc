package pt.ipc_app.session

import android.content.Context
import pt.ipc_app.domain.user.Role

/**
 * Session manager that uses shared preferences to store the session.
 *
 * @param context the application context
 *
 * @property token the user's token
 * @property username the user's username
 */
class SessionManagerSharedPrefs(private val context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE)
    }

    val token: String?
        get() = prefs.getString(TOKEN, null)

    val username: String?
        get() = prefs.getString(NAME, null)

    val role: String?
        get() = prefs.getString(ROLE, null)

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = token != null

    /**
     * Updates the session with the given tokens and username.
     *
     * @param token the user's token
     * @param name the user's name
     */
    fun setSession(
        token: String,
        name: String,
        role: Role
    ) {
        prefs.edit()
            .putString(TOKEN, token)
            .putString(NAME, name)
            .putString(ROLE, role.name)
            .apply()
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        prefs.edit()
            .remove(TOKEN)
            .remove(NAME)
            .remove(ROLE)
            .apply()
    }

    companion object {
        private const val SESSION_PREFS = "session"
        private const val TOKEN = "token"
        private const val NAME = "name"
        private const val ROLE = "role"
    }
}
