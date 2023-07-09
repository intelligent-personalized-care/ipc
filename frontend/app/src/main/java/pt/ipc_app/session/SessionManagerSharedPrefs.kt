package pt.ipc_app.session

import android.content.Context
import pt.ipc_app.domain.user.Role
import pt.ipc_app.domain.user.toRole
import java.util.UUID

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
            val savedAccessToken = prefs.getString(ACCESS_TOKEN, null)
            val savedRefreshToken = prefs.getString(REFRESH_TOKEN, null)
            val savedRole = prefs.getString(ROLE, null)

            return getUserInfo(savedId, savedName, savedAccessToken, savedRefreshToken, savedRole)
        }

        set(value) {
            if (value == null)
                prefs.edit()
                    .remove(ID)
                    .remove(NAME)
                    .remove(ACCESS_TOKEN)
                    .remove(REFRESH_TOKEN)
                    .remove(ROLE)
                    .apply()
            else
                prefs.edit()
                    .putString(ID, value.id)
                    .putString(NAME, value.name)
                    .putString(ACCESS_TOKEN, value.accessToken)
                    .putString(REFRESH_TOKEN, value.refreshToken)
                    .putString(ROLE, value.role.name)
                    .apply()
        }

    lateinit var userLoggedIn: UserInfo
    lateinit var userUUID: UUID

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = (userInfo != null).also {
        if (it) {
            userLoggedIn = userInfo!!
            userUUID = UUID.fromString(userLoggedIn.id)
        }
    }

    /**
     * Updates the session with the given tokens and username.
     *
     * @param id the user's id
     * @param name the user's name
     * @param accessToken the user's access token
     * @param refreshToken the user's refresh token
     * @param role the user's role
     */
    fun setSession(
        id: String,
        name: String,
        accessToken: String,
        refreshToken: String,
        role: Role
    ) {
        userInfo = UserInfo(id, name, accessToken, refreshToken, role)
        userLoggedIn = userInfo!!
        userUUID = UUID.fromString(userLoggedIn.id)
    }

    /**
     * Updates the session with the given tokens.
     *
     * @param accessToken the user's token
     */
    fun updateTokens(
        accessToken: String,
        refreshToken: String
    ) {
        userInfo = userInfo!!.copy(accessToken = accessToken, refreshToken = refreshToken)
        userLoggedIn = userInfo!!
        userUUID = UUID.fromString(userLoggedIn.id)
    }

    /**
     * Clears the session.
     */
    fun clearSession() {
        userInfo = null
    }

    private fun getUserInfo(id: String?, name: String?, accessToken: String?, refreshToken: String?, role: String?): UserInfo? {
        return if (id != null && name != null && accessToken != null && refreshToken != null && role != null) {
            val roleValidation = role.toRole() ?: return null
            UserInfo(
                id = id,
                name = name,
                accessToken = accessToken,
                refreshToken = refreshToken,
                role = roleValidation
            )
        } else null
    }

    companion object {
        private const val SESSION_PREFS = "session"

        private const val ID = "id"
        private const val NAME = "name"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val ROLE = "role"
    }
}
