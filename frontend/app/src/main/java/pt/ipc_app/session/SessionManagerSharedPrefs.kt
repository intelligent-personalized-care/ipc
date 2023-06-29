package pt.ipc_app.session

import android.content.Context
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.toRole

/**
 * Session manager that uses shared preferences to store the session.
 *
 * @param context the application context
 */
class SessionManagerSharedPrefs(private val context: Context) {

    private val prefs by lazy {
        context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE)
    }

    private var userInfo: UserInfo?
        get() {
            val savedId = prefs.getString(ID, null)
            val savedName = prefs.getString(NAME, null)
            val savedToken = prefs.getString(TOKEN, null)
            val savedRole = prefs.getString(ROLE, null)

            return getUserInfo(savedId, savedName, savedToken, savedRole)
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(ID)
                    .remove(NAME)
                    .remove(TOKEN)
                    .remove(ROLE)
                    .apply()
            else
                prefs.edit()
                    .putString(ID, value.id)
                    .putString(NAME, value.name)
                    .putString(TOKEN, value.token)
                    .putString(ROLE, value.role.name)
                    .apply()
        }

    lateinit var userLoggedIn: UserInfo

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
        id: String,
        name: String,
        token: String,
        role: Role
    ) {
        userInfo = UserInfo(id, name, token, role)
        userLoggedIn = userInfo!!
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        userInfo = null
    }

    private fun getUserInfo(id: String?, name: String?, token: String?, role: String?): UserInfo? {
        return if (id != null && name != null && token != null && role != null) {
            val roleValidation = role.toRole() ?: return null
            UserInfo(
                id = id,
                name = name,
                token = token,
                role = roleValidation
            )
        } else null
    }

    companion object {
        private const val SESSION_PREFS = "session"

        private const val ID = "id"
        private const val NAME = "name"
        private const val TOKEN = "token"
        private const val ROLE = "role"
    }
}
