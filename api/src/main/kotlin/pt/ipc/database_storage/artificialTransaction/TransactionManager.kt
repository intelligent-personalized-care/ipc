package pt.ipc.database_storage.artificialTransaction

import java.util.UUID

interface TransactionManager {

    fun <R> runBlock(block : (Transaction) -> R, fileName : UUID? = null) : R

}