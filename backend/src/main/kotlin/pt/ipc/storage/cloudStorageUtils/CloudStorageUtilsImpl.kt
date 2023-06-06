package pt.ipc.storage.cloudStorageUtils

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Component
import pt.ipc.domain.exceptions.ExerciseVideoNotExists
import java.io.ByteArrayOutputStream
import java.util.*

@Component
class CloudStorageUtilsImpl(
    cloudStorageConfiguration: CloudStorageConfiguration
) : CloudStorageUtils {

    private val storage: Storage = cloudStorageConfiguration.storage

    private val userPhotosBucket = cloudStorageConfiguration.userPhotosBucket
    private val monitorCredentialsBucket = cloudStorageConfiguration.monitorCredentialsBucket
    private val clientsVideosBucket = cloudStorageConfiguration.clientsVideosBucket
    private val exercisesPreviewsBucket = cloudStorageConfiguration.exercisesPreviewsBucket

    private val videoContentType = "video/mp4"
    private val pdfContentType = "application/pdf"
    private val pngContenType = "image/png"

    private fun upload(fileName: String, content: ByteArray, contentType: String, bucketName: String) {
        val blobId = BlobId.of(bucketName, fileName)

        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        storage.create(blobInfo, content)
    }

    private fun download(fileName: String, bucketName: String): ByteArray {
        val blob = storage.get(bucketName, fileName) ?: throw ExerciseVideoNotExists

        val outputStream = ByteArrayOutputStream()

        blob.downloadTo(outputStream)
        outputStream.close()

        return outputStream.toByteArray()
    }

    override fun uploadClientVideo(fileName: UUID, byteArray: ByteArray) =
        upload(fileName = fileName.toString(), content = byteArray, contentType = videoContentType, bucketName = clientsVideosBucket)

    override fun downloadClientVideo(fileName: UUID): ByteArray =
        download(fileName = fileName.toString(), bucketName = clientsVideosBucket)

    override fun uploadMonitorCredentials(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName.toString(), content = file, contentType = pdfContentType, bucketName = monitorCredentialsBucket)

    override fun downloadMonitorCredentials(fileName: UUID): ByteArray =
        download(fileName = fileName.toString(), bucketName = monitorCredentialsBucket)

    override fun downloadExampleVideo(exerciseID: UUID): ByteArray =
        download(fileName = exerciseID.toString(), bucketName = exercisesPreviewsBucket)

    override fun deleteWithID(fileName: UUID) {
        val buckets = listOf(userPhotosBucket, monitorCredentialsBucket, clientsVideosBucket)

        for (bucketName in buckets) {
            val blobToDelete: BlobId? = storage.list(bucketName)
                .iterateAll()
                .find { blob -> blob.name == fileName.toString() }
                ?.let { BlobId.of(bucketName, it.name) }

            if (blobToDelete != null) storage.delete(blobToDelete)
        }
    }

    override fun uploadProfilePicture(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName.toString(), content = file, contentType = pngContenType, bucketName = userPhotosBucket)

    override fun downloadProfilePicture(fileName: UUID): ByteArray =
        download(fileName = fileName.toString(), bucketName = userPhotosBucket)
}
