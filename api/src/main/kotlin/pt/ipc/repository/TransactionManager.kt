package pt.ipc.repository

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}