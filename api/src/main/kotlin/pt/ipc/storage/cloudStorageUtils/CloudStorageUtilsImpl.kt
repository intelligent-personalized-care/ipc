package pt.ipc.storage.cloudStorageUtils

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Component
import pt.ipc.domain.ExerciseVideoNotExists
import java.io.ByteArrayOutputStream
import java.util.*

@Component
class CloudStorageUtilsImpl(
    cloudStorageConfiguration: CloudStorageConfiguration
) : CloudStorageUtils {

    private val storage: Storage = cloudStorageConfiguration.storage
    private val bucketName = cloudStorageConfiguration.bucketName

    private val videoContentType = "video/mp4"
    private val pdfContentType = "application/pdf"
    private val pngContenType = "image/png"

    private val clientsVideosFolder = "clients_videos"
    private val monitorCredentialsFolder = "monitor_certifications"
    private val exampleVideoFolder = "example_videos"
    private val userProfilePicturesFolder = "users_profile_pictures"

    private fun upload(fileName: UUID, content: ByteArray, contentType: String, folder: String) {
        val blobId = BlobId.of(bucketName, "$folder/$fileName")

        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        storage.create(blobInfo, content)
    }

    private fun download(fileName: UUID, folder: String): ByteArray {
        val blob = storage.get(bucketName, "$folder/$fileName") ?: throw ExerciseVideoNotExists

        val outputStream = ByteArrayOutputStream()

        blob.downloadTo(outputStream)
        outputStream.close()

        return outputStream.toByteArray()
    }

    override fun uploadClientVideo(fileName: UUID, byteArray: ByteArray) =
        upload(fileName, byteArray, videoContentType, clientsVideosFolder)

    override fun downloadClientVideo(fileName: UUID): ByteArray =
        download(fileName, clientsVideosFolder)

    override fun uploadMonitorCredentials(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName, content = file, contentType = pdfContentType, folder = monitorCredentialsFolder)

    override fun downloadMonitorCredentials(fileName: UUID): ByteArray =
        download(fileName = fileName, folder = monitorCredentialsFolder)

    override fun downloadExampleVideo(exerciseName: UUID): ByteArray =
        download(fileName = exerciseName, folder = exampleVideoFolder)

    override fun deleteWithID(fileName: UUID) {
        val blobToDelete = storage.list(bucketName)
            .iterateAll()
            .find { blob -> blob.name == "$monitorCredentialsFolder/$fileName" || blob.name == "$clientsVideosFolder/$fileName" || blob.name == "$userProfilePicturesFolder/$fileName" }
            ?.let { BlobId.of(bucketName, it.name) }

        if (blobToDelete != null) {
            storage.delete(blobToDelete)
        }
    }

    override fun uploadProfilePicture(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName, content = file, contentType = pngContenType, folder = userProfilePicturesFolder)
}
