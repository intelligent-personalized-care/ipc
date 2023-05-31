package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.ConnectionRequestInput
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.models.PlanOutput
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
        password: String
    ): APIResult<RegisterOutput> =
        post(
            uri = "/users/monitors",
            body = RegisterMonitorInput(
                name = name,
                email = email,
                password = password
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
        clientId: String,
        token: String
    ): APIResult<MonitorOutput> =
        get(
            uri = "/users/clients/$clientId/monitor",
            token = token
        )

    /**
     * Search monitors available.
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun searchMonitorsAvailable(
        name: String,
        token: String
    ): APIResult<MonitorOutput> =
        get(
            uri = "/users/monitors?name=$name",
            token = token
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
            uri = "/users/clients/$clientId/plans?date=2023-05-29",
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

    /**
     * Connects the monitor with the client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun updateProfilePicture(
        image: File,
        clientId: UUID,
        token: String
    ): APIResult<FileOutput> =
        postWithFile(
            uri = "/users/clients/$clientId/profile/photo",
            token = token,
            file = image
        )

}

class FileOutput()
