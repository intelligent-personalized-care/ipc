package pt.ipc_app

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.ipc_app.service.IPCService
import pt.ipc_app.session.SessionManagerSharedPrefs
import java.util.concurrent.TimeUnit

/**
 * The contract for the object that holds all the globally relevant dependencies.
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 * @property sessionManager the manager used to handle the user session
 * @property services the service used to handle the ipc application
 */
interface DependenciesContainer {
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

    override val jsonEncoder: Gson = GsonBuilder().create()

    override val sessionManager = SessionManagerSharedPrefs(this)

    override val services = IPCService(
        apiEndpoint = API_ENDPOINT,
        httpClient = OkHttpClient.Builder().connectTimeout(300, TimeUnit.SECONDS).build(),
        jsonEncoder = jsonEncoder
    )

    companion object {
        private const val API_ENDPOINT = "https://9ffe-2a01-11-8120-4ce0-7963-322f-6b07-1390.ngrok-free.app"
    }
}
