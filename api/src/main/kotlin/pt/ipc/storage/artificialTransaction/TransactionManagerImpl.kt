package pt.ipc.storage.artificialTransaction

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import pt.ipc.storage.cloudStorageUtils.CloudStorageConfiguration
import java.util.*

@Component
class TransactionManagerImpl(
    private val jdbi: Jdbi,
    private val cloudStorageConfiguration: CloudStorageConfiguration
) : TransactionManager {

    override fun <R> runBlock(block: (Transaction) -> R, fileName: UUID?): R {
        return jdbi.inTransaction<R, Exception> { handle ->

            val transaction = TransactionImpl(handle, cloudStorageConfiguration)

            try {
                block(transaction)
            } catch (err: Exception) {
                fileName?.let { transaction.cloudStorage.deleteWithID(fileName = fileName) }
                throw err
            }
        }
    }
}
