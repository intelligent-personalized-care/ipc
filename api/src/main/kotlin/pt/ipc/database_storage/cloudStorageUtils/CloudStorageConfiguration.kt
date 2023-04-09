package pt.ipc.database_storage.cloudStorageUtils

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths


@Configuration
class CloudStorageConfiguration(

    @Value("\${server.config.secrets.google-project-id}")
    private val projectId : String

){

    private val credentialsPath : String = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")

    private final val credentials : GoogleCredentials = GoogleCredentials.fromStream(Paths.get(credentialsPath).toFile().inputStream())

    val storage: Storage = StorageOptions.newBuilder()
        .setCredentials(credentials)
        .setProjectId(projectId)
        .build()
        .service

    val bucketName = "ipc_storage"


}


