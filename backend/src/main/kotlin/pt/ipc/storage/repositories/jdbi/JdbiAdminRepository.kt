package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.User
import pt.ipc.services.dtos.MonitorInfo
import pt.ipc.storage.repositories.AdminRepository
import java.util.*

class JdbiAdminRepository(
    private val handle: Handle
) : AdminRepository {

    override fun getUserByIDAndSession(id: UUID, sessionID: String): User? =
        handle.createQuery(
            "select u.id,u.name,u.email,u.password_hash from dbo.users u inner join dbo.admin a on u.id = a.id " +
                "inner join dbo.session s on u.id = s.user_id " +
                " where s.user_id = :id and s.session = :sessionID"
        )
            .bind("id", id)
            .bind("sessionID", sessionID)
            .mapTo<User>()
            .singleOrNull()

    override fun createAdmin(
        id: UUID,
        email: String,
        name: String,
        passwordHash: String,
        sessionID: UUID
    ) {
        handle.createUpdate("insert into dbo.users(id, name, email, password_hash) values(:id, :name, :email, :passwordHash)")
            .bind("id", id)
            .bind("name", name)
            .bind("email", email)
            .bind("passwordHash", passwordHash)
            .execute()

        handle.createUpdate("insert into dbo.admin(id) values(:id)").bind("id", id).execute()

        handle.createUpdate("insert into dbo.session(user_id, session) values(:userID, :sessionID)")
            .bind("userID", id)
            .bind("sessionID", sessionID)
            .execute()
    }

    override fun getUnverifiedMonitors(): List<MonitorInfo> =
        handle.createQuery(
            "select id,name from dbo.users u inner join dbo.docs_authenticity da on u.id = da.monitor_id where da.state = 'waiting' order by da.dt_submit"
        )
            .mapTo<MonitorInfo>()
            .toList()

    override fun decideMonitorVerification(monitorID: UUID, decision: Boolean) {
        val state = if (decision) "valid" else "invalid"

        handle.createUpdate("update dbo.docs_authenticity set state = :state where monitor_id = :monitorID")
            .bind("state", state)
            .bind("monitorID", monitorID)
            .execute()
    }
}
