package pt.ipc_app.service.utils

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
    val status: Int
): ResponseError(title)

class NoInternetConnection(
    title: String = "No Internet Connection",
    message: String = "Please check your internet connection and try again."
): ResponseError(title, message)

object Errors {
    const val emailAlreadyExists = "This email already exists"
}
