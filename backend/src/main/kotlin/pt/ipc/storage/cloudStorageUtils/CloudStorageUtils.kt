package pt.ipc.storage.cloudStorageUtils

import java.util.*

interface CloudStorageUtils {

    fun uploadClientVideo(fileName: UUID, video: ByteArray)

    fun downloadClientVideo(fileName: UUID): ByteArray

    fun uploadMonitorCredentials(fileName: UUID, file: ByteArray)

    fun downloadMonitorCredentials(fileName: UUID): ByteArray

    fun downloadExampleVideo(exerciseID: UUID): ByteArray

    fun deleteWithID(fileName: UUID)

    fun uploadProfilePicture(fileName: UUID, file: ByteArray)

    fun downloadProfilePicture(fileName: UUID): ByteArray

    fun uploadVideoPreview(fileName: UUID, file: ByteArray)

    fun getAllCredentialsIDs() : List<UUID>

    fun deleteCredential(fileName : UUID)

    fun getClientsVideosIDs() : List<UUID>

    fun deleteClientVideo(fileName: UUID)

    fun getUserPhotosIDs() : List<UUID>

    fun deleteUserPicture(fileName: UUID)

    fun getVideoPreviewsIDs() : List<UUID>

    fun deleteVideoPreview(fileName: UUID)
}
