package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.register.RegisterOutput
import java.io.IOException

/**
 * The service that handles the plan functionalities.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class PlansService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Gets the plan of client.
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getPlanOfClient(
        planId: Int
    ): APIResult<RegisterOutput> =
        get(
            uri = ""
        )

}