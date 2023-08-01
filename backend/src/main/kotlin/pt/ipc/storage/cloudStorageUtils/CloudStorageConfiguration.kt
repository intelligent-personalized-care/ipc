package pt.ipc.storage.cloudStorageUtils

import com.google.api.gax.retrying.RetrySettings
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.threeten.bp.Duration


class CloudStorageConfiguration{
    companion object{

        private const val maxAttempts = 5

        private const val retryDelayMultiplier = 3.0

        private val totalTimeOut: Duration = Duration.ofMinutes(3)

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
            .setProjectId(System.getenv("GOOGLE_PROJECT_ID"))
            .build()

        val storage: Storage = storageOptions.service

        const val userPhotosBucket = "ipc_users_photos"
        const val monitorCredentialsBucket = "ipc_monitors_credentials"
        const val clientsVideosBucket = "ipc_clients_videos"
        const val exercisesPreviewsBucket = "ipc_exercises_previews"

    }
}
