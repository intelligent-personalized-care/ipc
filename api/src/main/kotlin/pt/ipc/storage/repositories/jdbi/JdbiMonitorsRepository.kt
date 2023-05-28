package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.User
import pt.ipc.http.models.RequestInformation
import pt.ipc.storage.repositories.MonitorRepository
import java.time.LocalDate
import java.util.*

class JdbiMonitorsRepository(
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

    override fun getMonitor(monitorID: UUID): MonitorDetails? =
        handle.createQuery("select id, name, email, photo_id from dbo.monitors m inner join dbo.users u on u.id = m.m_id where m.m_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<MonitorDetails>()
            .singleOrNull()

    override fun getMonitorOfClient(clientId: UUID): MonitorDetails? {
        val monitorId = handle.createQuery(
            """
                select monitor_id from dbo.client_to_monitor where client_id = :clientID
            """.trimIndent()
        )
            .bind("clientID", clientId)
            .mapTo<UUID>()
            .singleOrNull() ?: return null

        return getMonitor(monitorId)
    }

    override fun getMonitorRanking(monitorID: UUID): Float =
        handle.createQuery(
            "select avg(stars) from dbo.monitor_rating where monitor_id = :monitorID"
        )
            .bind("monitorID", monitorID)
            .mapTo<Float>()
            .singleOrNull() ?: 0F

    override fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int): List<MonitorDetails> {
        val queryName = if (name != null) "and u.name like :name" else ""

        return handle.createQuery(
            """
                    select id, name, email, photo_id from dbo.monitors m
                    inner join dbo.users u on u.id = m.m_id
                    inner join dbo.docs_authenticity da on m.m_id = da.monitor_id
                    where da.state = 'valid' $queryName
                    offset :skip
                    limit :limit
            """.trimIndent()
        )
            .bind("name", "%$name%")
            .bind("skip", skip)
            .bind("limit", limit)
            .mapTo<MonitorDetails>()
            .toList()
    }

    override fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, accept: Boolean) {
        handle.createUpdate("delete from dbo.client_requests where request_id = :requestID ")
            .bind("requestID", requestID)
            .execute()

        if (accept) {
            handle.createUpdate("insert into dbo.client_to_monitor values (:monitorID,:clientID)")
                .bind("monitorID", monitorID)
                .bind("clientID", clientID)
                .execute()
        }
    }

    override fun getRequestInformation(requestID: UUID): RequestInformation? {
        return handle.createQuery("select client_id,monitor_id,request_id,request_text from dbo.client_requests where request_id = :requestID")
            .bind("requestID", requestID)
            .mapTo<RequestInformation>()
            .singleOrNull()
    }

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        handle.createQuery("select client_id, monitor_id, request_id from dbo.client_requests where monitor_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<RequestInformation>()
            .toList()

    override fun checkIfMonitorIsVerified(monitorID: UUID): Boolean =
        handle.createQuery("select count(*) from dbo.docs_authenticity where monitor_id = :monitorID and state = 'valid' ")
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun checkIfIsMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean =
        handle.createQuery("select count(*) from dbo.client_to_monitor where monitor_id = :monitorID and client_id = :clientID")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .mapTo<Int>()
            .single() == 1
}
