package pt.ipc.repository.jdbi

import org.jdbi.v3.core.Handle
import pt.ipc.repository.Transaction

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    //override val usersRepository: UsersRepository by lazy { JdbiUsersRepository(handle) }

    //override val gamesRepository: GamesRepository by lazy { JdbiGamesRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}
