package pt.ipc.storage.transaction

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.util.*

@Component
class TransactionManagerImpl(
    private val jdbi: Jdbi,
) : TransactionManager {

    override fun <R> run(fileName: UUID?, block: (Transaction) -> R): R {
        return jdbi.inTransaction<R, Exception> { handle ->

            val transaction = TransactionImpl(handle)

            try {
                block(transaction)
            } catch (err: Exception) {
                fileName?.let { transaction.cloudStorage.deleteWithID(fileName = fileName) }
                throw err
            }
        }
    }
}
