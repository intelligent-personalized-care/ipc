package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.ConnectionRequestInput
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.service.models.register.RegisterClientInput
import pt.ipc_app.service.models.register.RegisterMonitorInput
import pt.ipc_app.service.models.register.RegisterOutput
import pt.ipc_app.service.models.users.ClientOutput
import pt.ipc_app.service.models.users.ClientsOfMonitor
import pt.ipc_app.service.models.users.ListMonitorsOutput
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
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMonitorOfClient(
        clientId: UUID,
        token: String
    ): APIResult<MonitorOutput> =
        get(
            uri = "/users/clients/$clientId/monitor",
            token = token
        )

    /**
     * Gets clients of monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getClientsOfMonitor(
        monitorId: UUID,
        token: String
    ): APIResult<ClientsOfMonitor> =
        get(
            uri = "/users/monitors/$monitorId/clients",
            token = token
        )

    /**
     * Search monitors available.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun searchMonitorsAvailable(
        name: String?,
        token: String
    ): APIResult<ListMonitorsOutput> =
        get(
            uri = "/users/monitors" + if (name != null) "?name=$name" else "",
            token = token
        )

    /**
     * Gets the current plan of client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getCurrentPlanOfClient(
        clientId: UUID,
        token: String
    ): APIResult<Plan> =
        get(
            uri = "/users/clients/$clientId/plans?date=2023-06-03",
            token = token
        )

    /**
     * Connects the monitor with the client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun connectMonitor(
        monitorId: UUID,
        clientId: UUID,
        token: String
    ): APIResult<Any> =
        post(
            uri = "/users/clients/$clientId/requests",
            token = token,
            body = ConnectionRequestInput(monitorId)
        )

    /**
     * Updates the profile picture of a client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun updateProfilePicture(
        image: File,
        clientId: UUID,
        token: String
    ): APIResult<Any> =
        postWithFile(
            uri = "/users/clients/$clientId/profile/photo",
            token = token,
            multipartPropName = "profilePicture",
            file = image,
            contentType = "image/jpeg"
        )

    /**
     * Submits the credential document of a monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun submitCredentialDocument(
        doc: File,
        monitorId: UUID,
        token: String
    ): APIResult<Any> =
        postWithFile(
            uri = "/users/monitors/$monitorId/credential",
            token = token,
            multipartPropName = "credential",
            file = doc,
            contentType = "application/pdf"
        )

}

