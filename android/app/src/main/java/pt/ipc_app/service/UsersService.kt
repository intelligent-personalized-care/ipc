package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.ConnectionRequestInput
import pt.ipc_app.service.models.register.PlanOutput
import pt.ipc_app.service.models.register.RegisterClientInput
import pt.ipc_app.service.models.register.RegisterMonitorInput
import pt.ipc_app.service.models.register.RegisterOutput
import java.io.File
import java.io.IOException
import java.util.UUID

/**
 * The service that handles the user functionalities.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class UsersService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Registers the client with the given [name], [email] and [password].
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun registerClient(
        name: String,
        email: String,
        password: String,
        weight: Int?,
        height: Int?,
        birthDate: String?,
        physicalCondition: String?
    ): APIResult<RegisterOutput> =
        post(
            uri = "/users/clients",
            body = RegisterClientInput(
                name = name,
                email = email,
                password = password,
                weight = weight,
                height = height,
                birthDate = birthDate,
                physicalCondition = physicalCondition
            )
        )

    /**
     * Registers the monitor with the given [name], [email] and [password].
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun registerMonitor(
        name: String,
        email: String,
        password: String,
        credential: File
    ): APIResult<RegisterOutput> =
        postWithFile(
            uri = "/users/monitors",
            body = RegisterMonitorInput(
                name = name,
                email = email,
                password = password,
                credential = credential
            )
        )

    /**
     * Gets the monitor of client.
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMonitorOfClient(

    ): APIResult<RegisterOutput> =
        get(
            uri = ""
        )

    /**
     * Gets the current plan of client.
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getCurrentPlanOfClient(
        clientId: String,
        token: String
    ): APIResult<PlanOutput> =
        get(
            uri = "/users/clients/$clientId/plans",
            token = token
        )


    /**
     * Connects the monitor with the client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun connectClient(
        id: UUID
    ): APIResult<RegisterOutput> =
        post(
            uri = "/users/monitors",
            body = ConnectionRequestInput(
                id
            )
        )
}