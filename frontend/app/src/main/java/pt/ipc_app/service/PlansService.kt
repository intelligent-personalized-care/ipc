package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.EmptyResponseBody
import pt.ipc_app.service.models.plans.CreatePlanOutput
import pt.ipc_app.service.models.plans.ListOfPlans
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.service.models.plans.PlanToClient
import java.io.IOException
import java.time.LocalDate
import java.util.UUID

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
     * Creates a plan.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun createPlan(
        plan: PlanInput,
        monitorId: UUID,
        token: String
    ): APIResult<CreatePlanOutput> =
        post(
            uri = "/users/monitors/$monitorId/plans",
            token = token,
            body = plan
        )

    /**
     * Gets a plan of client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getPlanOfClient(
        clientId: UUID,
        date: String,
        token: String
    ): APIResult<Plan> =
        get(
            uri = "/users/clients/$clientId/plans?date=$date",
            token = token
        )

    /**
     * Associates plan to a client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun associatePlanToClient(
        monitorId: UUID,
        clientId: UUID,
        token: String,
        planId: Int,
        startDate: String
    ): APIResult<EmptyResponseBody> =
        post(
            uri = "/users/monitors/$monitorId/clients/$clientId/plans",
            token = token,
            body = PlanToClient(planId, startDate)
        )

    /**
     * Gets plans of monitor.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getMonitorPlans(
        monitorId: UUID,
        token: String
    ): APIResult<ListOfPlans> =
        get(
            uri = "/users/monitors/$monitorId/plans",
            token = token
        )

}