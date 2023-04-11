package pt.ipc_app.service.connection

import pt.ipc_app.service.models.ProblemJson

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

/**
 * Checks if the api result is a success.
 *
 * @receiver the api result to check
 * @return true if the result is a success, false otherwise
 */
fun <T> APIResult<T>.isSuccess(): Boolean = this is APIResult.Success

/**
 * Checks if the api result is a failure.
 *
 * @receiver the api result to check
 * @return true if the result is a failure, false otherwise
 */
fun <T> APIResult<T>.isFailure(): Boolean = this is APIResult.Failure
