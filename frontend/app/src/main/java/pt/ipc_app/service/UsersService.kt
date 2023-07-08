package pt.ipc_app.service

import android.content.Context
import coil.request.ImageRequest
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.domain.Plan
import pt.ipc_app.domain.user.Role
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.connection.AUTHORIZATION
import pt.ipc_app.service.models.ConnectionRequestInput
import pt.ipc_app.service.models.exercises.ExercisesOfClients
import pt.ipc_app.service.models.login.LoginInput
import pt.ipc_app.service.models.login.LoginOutput
import pt.ipc_app.service.models.register.RegisterClientInput
import pt.ipc_app.service.models.register.RegisterMonitorInput
import pt.ipc_app.service.models.register.RegisterOutput
import pt.ipc_app.service.models.requests.ConnectionRequestDecisionInput
import pt.ipc_app.service.models.requests.RequestsOfMonitor
import pt.ipc_app.service.models.users.*
import pt.ipc_app.service.utils.ContentType
import pt.ipc_app.service.utils.MultipartEntry
import java.io.File
import java.io.IOException
import java.time.LocalDate
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
     * Logs in the user with the given [email] and [password].
     *
     * @return the API result of the register request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun login(
        email: String,
        password: String
    ): APIResult<LoginOutput> =
        post(
            uri = "/users/login",
            body = LoginInput(
                email = email,
                password = password
            )
        )


    /**
     * Gets the client profile.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getClientProfile(
        clientId: UUID,
        token: String
    ): APIResult<ClientOutput> =
        get(
            uri = "/users/clients/$clientId/profile",
            token = token
        )

    /**
     * Gets the monitor profile.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMonitorProfile(
        monitorId: UUID,
        token: String
    ): APIResult<MonitorProfile> =
        get(
            uri = "/users/monitors/$monitorId/profile",
            token = token
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
     * Gets client's details of monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getClientOfMonitor(
        monitorId: UUID,
        clientId: UUID,
        token: String
    ): APIResult<ClientOfMonitor> =
        get(
            uri = "/users/monitors/$monitorId/clients/$clientId",
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
    suspend fun connectMonitor(
        monitorId: UUID,
        clientId: UUID,
        token: String
    ): APIResult<Any> =
        post(
            uri = "/users/monitors/$monitorId",
            token = token,
            body = ConnectionRequestInput(clientId)
        )

    /**
     * Gets all monitor requests of monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMonitorRequests(
        monitorId: UUID,
        token: String
    ): APIResult<RequestsOfMonitor> =
        get(
            uri = "/users/monitors/$monitorId/requests",
            token = token
        )

    /**
     * Gets all exercises of clients of monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getExercisesOfClients(
        monitorId: UUID,
        date: LocalDate,
        token: String
    ): APIResult<ExercisesOfClients> =
        get(
            uri = "/users/monitors/$monitorId/clients/exercises?date=$date",
            token = token
        )

    /**
     * Decide connection request.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun decideConnectionRequest(
        monitorId: UUID,
        requestId: UUID,
        requestDecision: ConnectionRequestDecisionInput,
        token: String
    ): APIResult<RequestsOfMonitor> =
        post(
            uri = "/users/monitors/$monitorId/requests/$requestId",
            token = token,
            body = requestDecision
        )

    /**
     * Rates the monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun rateMonitor(
        monitorId: UUID,
        clientId: UUID,
        stars: Int,
        token: String
    ): APIResult<Any> =
        post(
            uri = "/users/monitors/$monitorId/rate",
            token = token,
            body = RatingInput(clientId, stars)
        )

    /**
     * Gets the profile picture of an user.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    fun getProfilePicture(
        context: Context,
        userId: UUID,
        token: String
    ): ImageRequest =
        ImageRequest.Builder(context)
            .data("$apiEndpoint/users/$userId/photo")
            .addHeader(AUTHORIZATION, "$BEARER_TOKEN $token")
            .build()

    /**
     * Updates the profile picture of an user.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun updateProfilePicture(
        image: File,
        userId: UUID,
        role: Role,
        token: String
    ): APIResult<Any> =
        postWithMultipartBody(
            uri = "/users/${role.name.lowercase()}s/$userId/profile/photo",
            token = token,
            multipartEntries = listOf(MultipartEntry(name = "photo", value = image, contentType = ContentType.IMAGE))
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
        postWithMultipartBody(
            uri = "/users/monitors/$monitorId/credential",
            token = token,
            multipartEntries = listOf(MultipartEntry(name = "credential", value = doc, contentType = ContentType.PDF))
        )

}

