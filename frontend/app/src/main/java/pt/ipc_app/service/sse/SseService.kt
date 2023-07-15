package pt.ipc_app.service.sse

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import pt.ipc_app.service.HTTPService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.connection.checkAuthorization
import pt.ipc_app.service.models.sse.*

/**
 * The service that handles the sse functionalities.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class SseService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    private lateinit var eventSource: EventSource

    private val eventSourceListener = object : EventSourceListener() {
        override fun onOpen(eventSource: EventSource, response: Response) {
            super.onOpen(eventSource, response)
            Log.d(TAG, "Connection Opened")
        }

        override fun onClosed(eventSource: EventSource) {
            super.onClosed(eventSource)
            Log.d(TAG, "Connection Closed")
        }

        override fun onEvent(
            eventSource: EventSource,
            id: String?,
            type: String?,
            data: String
        ) {
            Log.d(TAG, "On Event Received! Id: $id, Data: $data")
            id?.let {
                // Deserialize the SSE event data based on the event type
                val event = when (it) {
                    CredentialAcceptance::class.java.simpleName -> jsonEncoder.fromJson(data, CredentialAcceptance::class.java)
                    MonitorFeedBack::class.java.simpleName -> jsonEncoder.fromJson(data, MonitorFeedBack::class.java)
                    PlanAssociation::class.java.simpleName -> jsonEncoder.fromJson(data, PlanAssociation::class.java)
                    PostedVideo::class.java.simpleName -> jsonEncoder.fromJson(data, PostedVideo::class.java)
                    RequestAcceptance::class.java.simpleName -> jsonEncoder.fromJson(data, RequestAcceptance::class.java)
                    RequestMonitor::class.java.simpleName -> jsonEncoder.fromJson(data, RequestMonitor::class.java)
                    else -> SseEvent()
                }
                EventBus.postEvent(event)
            }
        }

        override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
            super.onFailure(eventSource, t, response)
            Log.d(TAG, "On Failure: ${response?.body}")
        }
    }

    // Start the SSE connection
    fun start(token: String) {
        val request = Request.Builder()
            .url("$apiEndpoint/users/subscribe")
            .checkAuthorization(BEARER_TOKEN, token)
            .header("Accept", "application/json; q=0.5")
            .header("Connection", "keep-alive")
            .build()

        eventSource = EventSources
            .createFactory(httpClient)
            .newEventSource(
                request = request,
                listener = eventSourceListener
            )
    }

    // Stop the SSE connection
    suspend fun stop(token: String): APIResult<Any> =
        post<Any>(
            uri = "/users/unsubscribe",
            token = token
        ).also { eventSource.cancel() }

    companion object {
        private const val TAG = "SSES"
    }
}