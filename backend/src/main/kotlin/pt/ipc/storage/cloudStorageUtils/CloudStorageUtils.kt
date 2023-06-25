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
}
