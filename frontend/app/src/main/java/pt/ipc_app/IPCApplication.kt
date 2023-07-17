package pt.ipc_app

import android.app.Application
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.ipc_app.service.IPCService
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.createNotification
import pt.ipc_app.ui.workers.RefreshTokensPeriodicWorker
import pt.ipc_app.ui.workers.SubscribeSSEPeriodicWorker
import java.util.concurrent.TimeUnit

/**
 * The contract for the object that holds all the globally relevant dependencies.
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property services the service used to handle the ipc application
 */
interface DependenciesContainer {
    val okHttp: OkHttpClient
    val jsonEncoder: Gson
    val sessionManager: SessionManagerSharedPrefs
    val services: IPCService
}

const val TAG = "IntelligentPersonalizedCare"

/**
 * The IPC application.
 *
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property services the service used to handle the ipc application
 */
class IPCApplication : DependenciesContainer, Application() {

    override val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.MINUTES)
            .writeTimeout(0, TimeUnit.MINUTES)
            .readTimeout(0, TimeUnit.MINUTES)
            .build()
    }

    override val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .create()
    }

    override val sessionManager: SessionManagerSharedPrefs by lazy {
        SessionManagerSharedPrefs(this)
    }

    override val services = IPCService(
        apiEndpoint = API_ENDPOINT,
        httpClient = okHttp,
        jsonEncoder = jsonEncoder
    )

    private val workerConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    override fun onCreate() {
        super.onCreate()

        createNotification(
            SSE_NOTIFICATIONS_CHANNEL,
            SSE_NOTIFICATIONS
        )

        if (sessionManager.isLoggedIn()) {
            val periodicWorkSubscribeRequest =
                PeriodicWorkRequestBuilder<SubscribeSSEPeriodicWorker>(
                    repeatInterval = SUBSCRIBE_REQUEST_REPEAT_HOURS,
                    repeatIntervalTimeUnit = TimeUnit.HOURS
                )
                    .setConstraints(workerConstraints)
                    .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                    SubscribeSSEPeriodicWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    periodicWorkSubscribeRequest
                )

            val periodicWorkLoginRequest =
                PeriodicWorkRequestBuilder<RefreshTokensPeriodicWorker>(
                    repeatInterval = LOGIN_REQUEST_REPEAT_MINUTES,
                    repeatIntervalTimeUnit = TimeUnit.MINUTES
                )
                .setConstraints(workerConstraints)
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                    RefreshTokensPeriodicWorker::class.java.simpleName,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorkLoginRequest
                )
        }
    }

    companion object {
        private const val API_ENDPOINT = "https://organic-byway-391719.ew.r.appspot.com"

        const val SSE_NOTIFICATIONS_CHANNEL = "sse_notifications_channel"
        private const val SSE_NOTIFICATIONS = "Notifications"

        private const val SUBSCRIBE_REQUEST_REPEAT_HOURS = 11L
        private const val LOGIN_REQUEST_REPEAT_MINUTES = 50L
    }
}
