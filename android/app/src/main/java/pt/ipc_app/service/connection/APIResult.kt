package pt.ipc_app.service.connection

import pt.ipc_app.service.utils.ProblemJson

/**
 * API Response result.
 *
 * @param T the type of the response
 */
sealed class APIResult<out T> {

    /**
     * The response was successful.
     *
     * @property data the response data
     */
    class Success<out T>(val data: T) : APIResult<T>()

    /**
     * The response was unsuccessful.
     *
     * @property error the ProblemJson object
     */
    class Failure(val error: ProblemJson) : APIResult<Nothing>()
}
