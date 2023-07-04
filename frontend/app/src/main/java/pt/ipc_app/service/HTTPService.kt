package pt.ipc_app.service

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.JsonReader
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pt.ipc_app.service.connection.*
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.service.utils.ProblemJson.Companion.problemJsonMediaType
import java.io.File
import java.io.IOException

/**
 * A service that communicates with a HTTP server.
 *
 * @property apiEndpoint the base URL of the API
 * @property httpClient the HTTP client used to communicate with the server
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
abstract class HTTPService(
    protected val apiEndpoint: String,
    protected val httpClient: OkHttpClient,
    val jsonEncoder: Gson
) {

    /**
     * Sends a HTTP request to the server and parses the response into a [APIResult].
     *
     * @receiver the HTTP request to send
     *
     * @return the result of the request
     * @throws IOException if there is an error while sending the request
     */
    protected suspend inline fun <reified T> Request.getResponseResult(): APIResult<T> =
        this.send(httpClient) { response ->
            val body = response.getBodyOrThrow()
            val resJson = JsonReader(body.charStream())

            try {
                if (response.isSuccessful && body.contentType() == applicationJsonMediaType)
                    APIResult.Success(jsonEncoder.fromJson(resJson, T::class.java))
                else if (body.contentType() == problemJsonMediaType)
                    APIResult.Failure(jsonEncoder.fromJson(resJson, ProblemJson::class.java))
                else
                    throw IllegalArgumentException()

            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                throw IllegalArgumentException()
            }
        }

    /**
     * Sends a GET request to the uri. If the [token] has an associated value, the request requires
     * authorization and will be sent in the header.
     *
     * @param uri the uri to send the request to
     * @param token if not null, the token to send in the header
     *
     * @return the result of the request
     * @throws IOException if there is an error while sending the request
     */
    protected suspend inline fun <reified T> get(
        uri: String,
        token: String? = null
    ): APIResult<T> =
        Request.Builder()
            .url(apiEndpoint + uri)
            .checkAuthorization(BEARER_TOKEN, token)
            .build()
            .getResponseResult()

    /**
     * Sends a POST request to the [uri] with the specified [body]. If the [token] has
     * an associated value, the request requires authorization and will be sent in the header.
     *
     * @param uri the uri to send the request to
     * @param token if not null, the token to send in the header
     * @param body the body to send in the request
     *
     * @return the result of the request
     * @throws IOException if there is an error while sending the request
     */
    protected suspend inline fun <reified T> post(
        uri: String,
        token: String? = null,
        body: Any
    ): APIResult<T> =
        Request.Builder()
            .url(apiEndpoint + uri)
            .checkAuthorization(BEARER_TOKEN, token)
            .post(
                jsonEncoder
                    .toJson(body)
                    .toRequestBody(applicationJsonMediaType)
            )
            .build()
            .getResponseResult()

    protected suspend inline fun <reified T> postWithFile(
        uri: String,
        token: String? = null,
        multipartPropName: String,
        file: File,
        contentType: String
    ): APIResult<T> {
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                multipartPropName,
                file.name,
                file.asRequestBody(contentType.toMediaTypeOrNull())
            )
            .build()

        return Request.Builder()
            .url(apiEndpoint + uri)
            .header("Content-Type", "multipart/form-data")
            .checkAuthorization(BEARER_TOKEN, token)
            .post(requestBody)
            .build()
            .getResponseResult()
    }

    companion object {
        private const val APPLICATION_JSON = "application/json"
        val applicationJsonMediaType = APPLICATION_JSON.toMediaType()

        const val BEARER_TOKEN = "Bearer"
    }
}

