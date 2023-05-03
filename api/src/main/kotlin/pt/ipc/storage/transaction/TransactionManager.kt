package pt.ipc.storage.transaction

import java.util.UUID

interface TransactionManager {

    fun <R> runBlock(block: (Transaction) -> R, fileName: UUID? = null): R
}
