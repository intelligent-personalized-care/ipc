package pt.ipc_app

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import pt.ipc_app.service.IPCService
import pt.ipc_app.session.SessionManagerSharedPrefs

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
        httpClient = OkHttpClient(),
        jsonEncoder = jsonEncoder
    )

    companion object {
        private const val API_ENDPOINT = "https://f52c-194-210-195-230.ngrok-free.app"
    }
}
