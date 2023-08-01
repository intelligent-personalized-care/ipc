package pt.ipc.storage.transaction

import java.util.UUID

interface TransactionManager {

    fun <R> run(fileName: UUID? = null, block: (Transaction) -> R): R
}
