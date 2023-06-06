package pt.ipc_app.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.ipc_app.domain.APIException
import pt.ipc_app.service.connection.APIResult

@Suppress("UNCHECKED_CAST")
fun <T> viewModelInit(block: () -> T) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return block() as T
        }
    }

/**
 * Tries to execute a request to the server.
 *
 * @return the result of the request
 */
suspend fun <T> executeRequest(
    request: suspend () -> APIResult<T>
): T {
    while (true) {
        val apiRes = request()

        if (apiRes is APIResult.Failure)
            throw APIException(apiRes.error)

        if (apiRes is APIResult.Success)
            return apiRes.data
    }
}
