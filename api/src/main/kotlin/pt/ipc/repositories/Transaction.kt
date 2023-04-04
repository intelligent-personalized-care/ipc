package pt.ipc.repositories

interface Transaction {

    val usersRepository: UsersRepository

    fun rollback()
}