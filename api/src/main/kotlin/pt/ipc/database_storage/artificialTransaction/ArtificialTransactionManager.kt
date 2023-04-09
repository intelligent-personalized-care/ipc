package pt.ipc.database_storage.artificialTransaction

import java.util.UUID

interface ArtificialTransactionManager {

    fun <R> runBlock(block : (ArtificialTransaction) -> R, fileName : UUID? = null) : R

}