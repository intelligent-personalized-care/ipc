package pt.ipc.database_storage.cloudStorageUtils

import java.util.*


interface CloudStorageUtils{

    fun uploadClientVideo(fileName: UUID, byteArray: ByteArray)

    fun downloadClientVideo(fileName: UUID) : ByteArray

    fun uploadMonitorCredentials(fileName: UUID, file : ByteArray)

    fun downloadMonitorCredentials(fileName: UUID) : ByteArray

    fun downloadExampleVideo(exerciseName : UUID) : ByteArray

    fun deleteWithID(fileName: UUID)

    fun uploadProfilePicture(fileName: UUID, file : ByteArray)

}