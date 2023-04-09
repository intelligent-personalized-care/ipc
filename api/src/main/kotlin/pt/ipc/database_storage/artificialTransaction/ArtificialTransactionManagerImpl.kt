package pt.ipc.database_storage.artificialTransaction

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.ipc.database_storage.cloudStorageUtils.CloudStorageConfiguration
import java.util.*

@Component
class ArtificialTransactionManagerImpl(
    private val jdbi: Jdbi,
    private val cloudStorageConfiguration: CloudStorageConfiguration
) : ArtificialTransactionManager {

    override fun <R> runBlock(block: (ArtificialTransaction) -> R, fileName: UUID?): R {

        return jdbi.inTransaction<R, Exception> { handle ->

            val transaction = ArtificialTransactionImpl(handle, cloudStorageConfiguration)

            try {

                block(transaction)

            } catch (err: Exception) {
                fileName?.let{ transaction.cloudStorage.deleteWithID(fileName = fileName) }
                throw err
            }
        }
    }
}