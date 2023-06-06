package pt.ipc_app.service.models

/**
 * The Authentication Output Model.
 *
 * @property id the user id
 * @property token the user token
 */
data class AuthenticationOutputModel(
    val id: Int,
    val token: String
)
