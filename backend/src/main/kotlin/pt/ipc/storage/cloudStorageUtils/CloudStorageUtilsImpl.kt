package pt.ipc.storage.cloudStorageUtils

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Component
import pt.ipc.domain.exceptions.FileDoesNotExists
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
    private val pngContentType = "image/png"

    private fun upload(fileName: UUID, content: ByteArray, contentType: String, bucketName: String) {
        val blobId = BlobId.of(bucketName, fileName.toString())

        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        storage.create(blobInfo, content)
    }

    private fun download(fileName: UUID, bucketName: String): ByteArray {
        val blob = storage.get(bucketName, fileName.toString()) ?: throw FileDoesNotExists

        val outputStream = ByteArrayOutputStream()

        blob.downloadTo(outputStream)
        outputStream.close()

        return outputStream.toByteArray()
    }

    private fun deleteFile(bucketName: String, fileName : String){
        BlobId.of(bucketName, fileName)?.let { storage.delete(it) }
    }

    override fun uploadClientVideo(fileName: UUID, video: ByteArray) =
        upload(fileName = fileName, content = video, contentType = videoContentType, bucketName = clientsVideosBucket)

    override fun downloadClientVideo(fileName: UUID): ByteArray =
        download(fileName = fileName, bucketName = clientsVideosBucket)

    override fun uploadMonitorCredentials(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName, content = file, contentType = pdfContentType, bucketName = monitorCredentialsBucket)

    override fun downloadMonitorCredentials(fileName: UUID): ByteArray =
        download(fileName = fileName, bucketName = monitorCredentialsBucket)

    override fun downloadExampleVideo(exerciseID: UUID): ByteArray =
        download(fileName = exerciseID, bucketName = exercisesPreviewsBucket)

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
        upload(fileName = fileName, content = file, contentType = pngContentType, bucketName = userPhotosBucket)

    override fun downloadProfilePicture(fileName: UUID): ByteArray =
        download(fileName = fileName, bucketName = userPhotosBucket)

    override fun uploadVideoPreview(fileName: UUID, file: ByteArray) =
        upload(fileName = fileName, file, contentType = videoContentType, exercisesPreviewsBucket)

    override fun getAllCredentialsIDs(): List<UUID> =
        storage.list(monitorCredentialsBucket).iterateAll().toList().map { UUID.fromString(it.name) }

    override fun deleteCredential(fileName: UUID) {
        deleteFile(bucketName = monitorCredentialsBucket,fileName = fileName.toString())
    }

    override fun getClientsVideosIDs(): List<UUID> =
        storage.list(clientsVideosBucket).iterateAll().toList().map { UUID.fromString(it.name) }

    override fun deleteClientVideo(fileName: UUID) =
        deleteFile(bucketName = clientsVideosBucket,fileName = fileName.toString())

    override fun getUserPhotosIDs(): List<UUID> =
        storage.list(userPhotosBucket).iterateAll().toList().map { UUID.fromString(it.name) }


    override fun deleteUserPicture(fileName: UUID) {
        deleteFile(bucketName = userPhotosBucket ,fileName = fileName.toString())
    }

    override fun getVideoPreviewsIDs(): List<UUID> =
        storage.list(exercisesPreviewsBucket).iterateAll().toList().map { UUID.fromString(it.name) }

    override fun deleteVideoPreview(fileName: UUID) {
        deleteFile(bucketName = exercisesPreviewsBucket,fileName = fileName.toString())
    }

}
