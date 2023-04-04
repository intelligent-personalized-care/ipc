package pt.ipc.repository

interface Transaction {

    //val usersRepository: UsersRepository

    //val gamesRepository: GamesRepository

    fun rollback()
}