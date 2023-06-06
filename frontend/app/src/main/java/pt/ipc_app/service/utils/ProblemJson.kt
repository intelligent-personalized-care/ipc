package pt.ipc_app.service.utils

import okhttp3.MediaType.Companion.toMediaType
import java.net.URI

/**
 * A problem that occurred during the processing of a request.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">Problem Details for HTTP APIs</a>
 */
data class ProblemJson(
    val type: URI,
    val title: String,
    val status: Int,
    val detail: String? = null,
    val instance: URI? = null
) {
    companion object {
        private const val PROBLEM_MEDIA_TYPE = "application/problem+json"

        val problemJsonMediaType = PROBLEM_MEDIA_TYPE.toMediaType()
    }
}

object Errors {
    val emailAlreadyExists = "This email already exists"
}
