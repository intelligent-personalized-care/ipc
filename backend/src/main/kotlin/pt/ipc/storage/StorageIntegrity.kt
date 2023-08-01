package pt.ipc.storage

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pt.ipc.storage.transaction.TransactionManager

@Component
class StorageIntegrity(
    private val transactionManager: TransactionManager
) {

    @Scheduled(fixedDelay = DAY)
    fun removeUsersPhotos() {
        transactionManager.run {
            val photosID = it.usersRepository.getUsersIDs()

            it.cloudStorage.getUserPhotosIDs().forEach { cloudID ->
                if (!photosID.contains(cloudID)) {
                    println("DELETING CLOUD PHOTO -> $cloudID")
                    it.cloudStorage.deleteUserPicture(fileName = cloudID)
                }
            }
        }
    }

    @Scheduled(fixedDelay = DAY)
    fun removeCredentials() {
        transactionManager.run {
            val docsIDs = it.monitorRepository.getAllCredentials()

            val cloudIDs = it.cloudStorage.getAllCredentialsIDs()

            cloudIDs.forEach { cloudID ->
                if (!docsIDs.contains(cloudID)) {
                    println("DELETING CLOUD CREDENTIAL -> $cloudID")
                    it.cloudStorage.deleteCredential(fileName = cloudID)
                }
            }

            docsIDs.forEach { docID ->
                if (!cloudIDs.contains(docID)) {
                    println("DELETING SQL CREDENTIAL -> $docID")
                    it.monitorRepository.deleteCredential(monitorID = docID)
                }
            }
        }
    }

    @Scheduled(fixedDelay = DAY)
    fun removeClientsVideos() {
        transactionManager.run {
            val clientsVideosIDs = it.clientsRepository.getClientsVideosIDs()
            val cloudIDs = it.cloudStorage.getClientsVideosIDs()

            cloudIDs.forEach { cloudID ->
                if (!clientsVideosIDs.contains(cloudID)) {
                    println("DELETING CLOUD CLIENT VIDEO -> $cloudID")
                    it.cloudStorage.deleteClientVideo(fileName = cloudID)
                }
            }

            clientsVideosIDs.forEach { videoID ->
                if (!cloudIDs.contains(videoID)) {
                    println("DELETING SQL CLIENT VIDEO -> $videoID")
                    it.clientsRepository.deleteClientVideoID(videoID = videoID)
                }
            }
        }
    }

    @Scheduled(fixedDelay = DAY)
    fun removeExercisesPreviews() {
        transactionManager.run {
            val cloudPreviews = it.cloudStorage.getVideoPreviewsIDs()
            val previewsIDs = it.exerciseRepository.getPreviewsIDs()

            cloudPreviews.forEach { cloudID ->
                if (!previewsIDs.contains(cloudID)) {
                    println("DELETING CLOUD PREVIEW VIDEO -> $cloudID")
                    it.cloudStorage.deleteVideoPreview(fileName = cloudID)
                }
            }

            previewsIDs.forEach { videoID ->
                if (!cloudPreviews.contains(videoID)) {
                    println("DELETING SQL PREVIEW VIDEO -> $videoID")
                    it.exerciseRepository.deletePreview(videoID = videoID)
                }
            }
        }
    }

    companion object {
        private const val DAY = 1000L * 60L * 60L * 24L // Every 24 hours
    }
}
