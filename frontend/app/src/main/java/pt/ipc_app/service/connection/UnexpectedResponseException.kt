package pt.ipc_app.service.connection

import okhttp3.Response

/**
 * An exception thrown when the server responds with an unexpected response.
 *
 * @property response the response that caused the exception
 */
class UnexpectedResponseException(private val response: Response) :
    Exception("Unexpected ${response.code} response from the server.")
