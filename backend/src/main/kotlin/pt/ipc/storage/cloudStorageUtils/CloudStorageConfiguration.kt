package pt.ipc.storage.cloudStorageUtils

import com.google.api.gax.retrying.RetrySettings
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.threeten.bp.Duration

@Configuration
class CloudStorageConfiguration(
    @Value("\${server.config.secrets.google-project-id}")
     projectID: String
) {

    private val retryStorageOptions: RetrySettings = StorageOptions
        .getDefaultRetrySettings()
        .toBuilder()
        .setMaxAttempts(maxAttempts)
        .setRetryDelayMultiplier(retryDelayMultiplier)
        .setTotalTimeout(totalTimeOut)
        .build()

    private val storageOptions: StorageOptions = StorageOptions
        .getDefaultInstance()
        .toBuilder()
        .setRetrySettings(retryStorageOptions)
        .setProjectId(projectID)
        .build()

    val storage: Storage = storageOptions.service


    companion object{

        private const val maxAttempts = 5

        private const val retryDelayMultiplier = 3.0

        private val totalTimeOut: Duration = Duration.ofMinutes(3)

        const val userPhotosBucket = "ipc_users_photos"
        const val monitorCredentialsBucket = "ipc_monitors_credentials"
        const val clientsVideosBucket = "ipc_clients_videos"
        const val exercisesPreviewsBucket = "ipc_exercises_previews"

    }
}
