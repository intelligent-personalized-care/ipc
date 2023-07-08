package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.storage.repositories.UsersRepository
import java.util.*

class JdbiUsersRepository(
    private val handle: Handle
) : UsersRepository {

    override fun getUserBySession(sessionID: UUID): UUID? =
        handle.createQuery("select user_id  from dbo.session s where s.session = :sessionID")
            .bind("sessionID", sessionID)
            .mapTo<UUID>()
            .singleOrNull()

    override fun updateSession(userID: UUID, sessionID: UUID) {
        handle.createUpdate(
            "insert into dbo.session (user_id, session) values (:userID,:sessionID)" +
                "on conflict(user_id) do update set session = :sessionID "
        )
            .bind("userID", userID)
            .bind("sessionID", sessionID)
            .execute()
    }

    override fun getUserByIDAndSession(id: UUID, sessionID: UUID): User? =
        handle.createQuery(
            "select u.id,u.name,u.email,u.password_hash from dbo.users u " +
                "inner join dbo.session s on s.user_id = u.id " +
                "where s.user_id = :id and s.session = :sessionID"
        )
            .bind("id", id)
            .bind("sessionID", sessionID)
            .mapTo<User>()
            .singleOrNull()

    override fun login(email: String, passwordHash: String): UUID? =
        handle.createQuery("select id from dbo.users where email = :email  and password_hash = :passwordHash")
            .bind("email", email)
            .bind("passwordHash", passwordHash)
            .mapTo<UUID>()
            .singleOrNull()

    override fun getUserByID(userID: UUID): User? =
        handle.createQuery("select id, name, email, password_hash from dbo.users where id = :userID")
            .bind("userID", userID)
            .mapTo<User>()
            .singleOrNull()

    override fun getRoleByID(userID: UUID): Role =
        handle.createQuery("select 'CLIENT' from dbo.clients where c_id = :userID")
            .bind("userID", userID)
            .mapTo<Role>()
            .singleOrNull()
            ?: handle.createQuery("select 'MONITOR' from dbo.monitors where m_id = :userID")
            .bind("userID", userID)
            .mapTo<Role>()
            .singleOrNull() ?: Role.ADMIN
}
