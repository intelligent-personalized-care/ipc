package pt.ipc_app.session

import android.content.Context

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
        get() = prefs.getString(USERNAME, null)

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = token != null

    /**
     * Updates the session with the given tokens and username.
     *
     * @param token the user's access token
     * @param username the user's username
     */
    fun setSession(
        token: String,
        username: String
    ) {
        prefs.edit()
            .putString(TOKEN, token)
            .putString(USERNAME, username)
            .apply()
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        prefs.edit()
            .remove(TOKEN)
            .remove(USERNAME)
            .apply()
    }

    companion object {
        private const val SESSION_PREFS = "session"
        private const val TOKEN = "token"
        private const val USERNAME = "username"
    }
}
