package pt.ipc_app.service.connection

import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Extension function used to send [this] request using [okHttpClient] and process the
 * received response with the given [handler]. Note that [handler] is called from a
 * OkHttp IO Thread.
 *
 * @receiver the request to be sent
 * @param okHttpClient  the client from where the request is sent
 * @param handler       the handler function, which is called from a IO thread.
 *                      Be mindful of threading issues.
 * @return the result of the response [handler]
 * @throws  [IOException] if a communication error occurs.
 * @throws  [Throwable] if any error is thrown by the response handler.
 */
suspend fun <T> Request.send(okHttpClient: OkHttpClient, handler: (Response) -> T): T =
    suspendCancellableCoroutine { continuation ->
        val call = okHttpClient.newCall(request = this)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.println(Log.WARN, "TAG", "e: " + e.message)
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    continuation.resume(handler(response))
                } catch (t: Throwable) {
                    Log.println(Log.WARN, "TAG", "t: " + t.message)
                    continuation.resumeWithException(t)
                }
            }
        })

        continuation.invokeOnCancellation { call.cancel() }
    }

fun Request.Builder.checkAuthorization(type: String, token: String?): Request.Builder =
    if (token != null)
        header("Authorization", "$type $token")
    else this


/**
 * Gets the [ResponseBody] from the [Response] body.
 *
 * @receiver the response
 * @return the response body
 * @throws IllegalStateException if the response body is null
 */
fun Response.getBodyOrThrow(): ResponseBody =
    body ?: throw IllegalStateException("Response body is null")
