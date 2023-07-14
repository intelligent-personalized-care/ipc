package pt.ipc_app.service.utils

import java.net.HttpURLConnection

open class ResponseError(
    val title: String,
    val message: String? = null
)

/**
 * A problem that occurred during the processing of a request.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">Problem Details for HTTP APIs</a>
 */
class ProblemJson(
    title: String,
    private val status: Int
): ResponseError(title) {

    fun unauthenticatedResponse() = title == UNAUTHENTICATED && status == HttpURLConnection.HTTP_UNAUTHORIZED

    companion object {
        private const val UNAUTHENTICATED = "Unauthenticated"
    }

}

class NoInternetConnection(
    title: String = "No Internet Connection",
    message: String = "Please check your internet connection and try again."
): ResponseError(title, message)

object Errors {
    const val emailAlreadyExists = "This email already exists"
}
