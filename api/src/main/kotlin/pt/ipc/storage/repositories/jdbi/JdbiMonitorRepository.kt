package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.User
import pt.ipc.storage.repositories.MonitorRepository
import java.time.LocalDate
import java.util.*

class JdbiMonitorRepository(
    private val handle: Handle
) : MonitorRepository {

    override fun registerMonitor(user: User, date: LocalDate, encryptedToken: String) {
        handle.createUpdate("insert into dbo.users values(:id,:u_name,:u_email,:password_hash)")
            .bind("id", user.id)
            .bind("u_name", user.name)
            .bind("u_email", user.email)
            .bind("password_hash", user.passwordHash)
            .execute()

        handle.createUpdate("insert into dbo.monitors values (:id)")
            .bind("id", user.id)
            .execute()

        handle.createUpdate("insert into dbo.tokens values(:token_hash,:user_id)")
            .bind("token_hash", encryptedToken)
            .bind("user_id", user.id)
            .execute()

        handle.createUpdate("insert into dbo.docs_authenticity values(:monitor_id,'waiting',:dt_submit)")
            .bind("monitor_id", user.id)
            .bind("dt_submit", date)
            .execute()
    }

    override fun requestClient(requestID: UUID, monitorID: UUID, clientID: UUID) {
        handle.createUpdate("insert into dbo.client_requests values(:monitorID, :clientID, :requestID)")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .bind("requestID", requestID)
            .execute()
    }

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        handle.createQuery("select client_id, monitor_id, request_id from dbo.client_requests where monitor_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<RequestInformation>()
            .toList()

    override fun checkIfMonitorIsVerified(monitorID: UUID) : Boolean =
        handle.createQuery("select count(*) from dbo.docs_authenticity where monitor_id = :monitorID and state = 'valid' ")
               .bind("monitorID",monitorID)
               .mapTo<Int>()
               .single() == 1


    override fun checkIfIsMonitorOfClient(monitorID: UUID, clientID: UUID) : Boolean =
        handle.createQuery("select count(*) from dbo.client_to_monitor where monitor_id = :monitorID and client_id = :clientID")
            .bind("monitorID",monitorID)
            .bind("clientID",clientID)
            .mapTo<Int>()
            .single() == 1
}
