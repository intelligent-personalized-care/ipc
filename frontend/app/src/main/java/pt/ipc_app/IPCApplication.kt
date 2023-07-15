package pt.ipc_app

import android.app.Application
import androidx.work.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.ipc_app.service.IPCService
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.workers.RefreshTokensPeriodicWorker
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

        if (sessionManager.isLoggedIn()) {
            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<RefreshTokensPeriodicWorker>(repeatInterval = 50, TimeUnit.MINUTES)
                .setConstraints(workerConstraints)
                .build()

            WorkManager.getInstance(applicationContext)
                .enqueueUniquePeriodicWork(
                    "LoginPeriodicWorker",
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorkRequest
                )
        }

    }

    companion object {
        private const val API_ENDPOINT = "https://organic-byway-391719.ew.r.appspot.com"
    }
}
