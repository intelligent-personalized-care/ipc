package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.plans.CreatePlanOutput
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.service.models.register.RegisterOutput
import java.io.IOException
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
            uri = "/users/monitors/$monitorId/clients/fb97992c-6195-4822-8499-eedb629cbbe5/plans",
            token = token,
            body = plan
        )



}