package pt.ipc.repositories.jdbi

import org.jdbi.v3.core.Handle
import pt.ipc.repositories.Transaction
import pt.ipc.repositories.UsersRepository

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: UsersRepository by lazy { JdbiUsersRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}
