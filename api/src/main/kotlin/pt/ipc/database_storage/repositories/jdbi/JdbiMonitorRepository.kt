package pt.ipc.database_storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import pt.ipc.database_storage.repositories.MonitorRepository
import pt.ipc.domain.User
import java.time.LocalDate

class JdbiMonitorRepository(
    private val handle : Handle
) : MonitorRepository {

    override fun registerMonitor(user: User, date : LocalDate, encryptedToken : String) {
        handle.createUpdate("insert into dbo.users values(:id,:u_name,:u_email,:password_hash)")
            .bind("id",user.id)
            .bind("u_name",user.name)
            .bind("u_email",user.email)
            .bind("password_hash",user.passwordHash)
            .execute()

        handle.createUpdate("insert into dbo.monitors values (:id)")
            .bind("id", user.id)
            .execute()

        handle.createUpdate("insert into dbo.tokens values(:token_hash,:user_id)")
            .bind("token_hash", encryptedToken)
            .bind("user_id", user.id)
            .execute()

        handle.createUpdate("insert into dbo.docs_authenticity values(:monitor_id,false,:dt_submit)")
            .bind("monitor_id", user.id)
            .bind("dt_submit",date)
            .execute()
    }

}