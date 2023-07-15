package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.sse.SseService

/**
 * The service that handles the ipc application.
 *
 * @param apiEndpoint the API endpoint
 * @param httpClient the HTTP client
 * @param jsonEncoder the JSON encoder used to serialize/deserialize objects
 *
 * @property usersService the service that handles the users
 */
open class IPCService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    val usersService = UsersService(apiEndpoint, httpClient, jsonEncoder)
    val plansService = PlansService(apiEndpoint, httpClient, jsonEncoder)
    val exercisesService = ExercisesService(apiEndpoint, httpClient, jsonEncoder)

    val sseService = SseService(apiEndpoint, httpClient, jsonEncoder)

}
