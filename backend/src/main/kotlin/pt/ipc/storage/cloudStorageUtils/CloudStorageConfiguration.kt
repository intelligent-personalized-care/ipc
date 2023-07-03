package pt.ipc.storage.cloudStorageUtils

import com.google.api.gax.retrying.RetrySettings
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.threeten.bp.Duration
import java.nio.file.Paths

@Configuration
class CloudStorageConfiguration(

    @Value("\${server.config.secrets.google-project-id}")
    private val projectID: String

) {

    private val maxAttempts = 5

    private val retryDelayMultiplier = 3.0

    private val totalTimeOut: Duration = Duration.ofMinutes(3)

    private val credentialsPath: String = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")

    private val credentials: GoogleCredentials = GoogleCredentials.fromStream(Paths.get(credentialsPath).toFile().inputStream())

    private val retryStorageOptions: RetrySettings = StorageOptions
        .getDefaultRetrySettings()
        .toBuilder()
        .setMaxAttempts(maxAttempts)
        .setRetryDelayMultiplier(retryDelayMultiplier)
        .setTotalTimeout(totalTimeOut)
        .build()

    private val storageOptions: StorageOptions = StorageOptions
        .newBuilder()
        .setRetrySettings(retryStorageOptions)
        .setCredentials(credentials)
        .setProjectId(projectID)
        .build()

    val storage: Storage = storageOptions.service

    val userPhotosBucket = "ipc_users_photos"
    val monitorCredentialsBucket = "ipc_monitors_credentials"
    val clientsVideosBucket = "ipc_clients_videos"
    val exercisesPreviewsBucket = "ipc_exercises_previews"
}
