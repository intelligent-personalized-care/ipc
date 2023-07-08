package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.mapper.reflect.ColumnName
import pt.ipc.domain.ClientExercises
import pt.ipc.domain.ClientOfMonitor
import pt.ipc.domain.DailyExercise
import pt.ipc.domain.MonitorDetails
import pt.ipc.domain.PlanOfClient
import pt.ipc.domain.User
import pt.ipc.http.models.ClientInformation
import pt.ipc.http.models.MonitorAvailable
import pt.ipc.http.models.MonitorProfile
import pt.ipc.http.models.Rating
import pt.ipc.http.models.RequestInformation
import pt.ipc.storage.repositories.MonitorRepository
import java.time.Duration
import java.time.LocalDate
import java.util.UUID

class JdbiMonitorsRepository(
    private val handle: Handle
) : MonitorRepository {

    override fun registerMonitor(user: User, sessionID: UUID) {
        handle.createUpdate("insert into dbo.users values(:id,:u_name,:u_email,:password_hash)")
            .bind("id", user.id)
            .bind("u_name", user.name)
            .bind("u_email", user.email)
            .bind("password_hash", user.passwordHash)
            .execute()

        handle.createUpdate("insert into dbo.monitors values (:id)")
            .bind("id", user.id)
            .execute()

        handle.createUpdate("insert into dbo.session(user_id, session) values(:userID, :sessionID)")
            .bind("userID", user.id)
            .bind("sessionID", sessionID)
            .execute()
    }

    override fun insertCredential(monitorID: UUID, dtSubmit: LocalDate) {
        handle.createUpdate("insert into dbo.docs_authenticity(monitor_id, state, dt_submit) values (:monitorID,'waiting',:dtSubmit)")
            .bind("monitorID", monitorID)
            .bind("dtSubmit", dtSubmit)
            .execute()
    }

    override fun getUserByIDAndSession(id: UUID, sessionID: UUID): User? =
        handle.createQuery(
            "select u.id,u.name,u.email,u.password_hash from dbo.users u " +
                "inner join dbo.monitors m on u.id = m.m_id " +
                "inner join dbo.session s on u.id = s.user_id " +
                " where s.user_id = :id and s.session = :sessionID "
        )
            .bind("id", id)
            .bind("sessionID", sessionID)
            .mapTo<User>()
            .singleOrNull()

    override fun getMonitorProfile(monitorID: UUID): MonitorProfile? {
        val details = handle.createQuery(
            "select u.id,u.name,u.email from dbo.monitors m " +
                "inner join dbo.users u on u.id = m.m_id " +
                "where u.id = :monitorID "
        )
            .bind("monitorID", monitorID)
            .mapTo<MonitorDetails>()
            .singleOrNull() ?: return null

        val rating = handle.createQuery(
            "SELECT avg(stars) AS averageStarts, count(*) AS nrOfReviews FROM dbo.monitor_rating WHERE monitor_id = :monitorID"
        )
            .bind("monitorID", monitorID)
            .mapTo<Rating>()
            .single()
            .isEmpty()

        val docState = handle.createQuery("select state from dbo.docs_authenticity where monitor_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<String>()
            .singleOrNull()

        return MonitorProfile(
            id = details.id,
            name = details.name,
            email = details.email,
            rating = rating,
            docState = docState
        )
    }

    override fun getClientOfMonitor(monitorID: UUID, clientID: UUID): ClientOfMonitor? {
        val clientInfo = handle.createQuery(
            "select u.name, u.email, c.weight, c.height, c.physical_condition, c.birth_date from dbo.users u " +
                "inner join dbo.client_to_monitor ctm on u.id = ctm.client_id " +
                "inner join dbo.clients c on u.id = c.c_id " +
                "where ctm.client_id = :clientID and ctm.monitor_id = :monitorID"
        )
            .bind("clientID", clientID)
            .bind("monitorID", monitorID)
            .mapTo<ClientInfo>()
            .singleOrNull() ?: return null

        val plans = handle.createQuery(
            "select p.id,p.title,cp.dt_start as startDate,cp.dt_end as endDate from dbo.clients c " +
                "inner join dbo.client_plans cp on c.c_id = cp.client_id " +
                "inner join dbo.plans p on p.id = cp.plan_id " +
                "where cp.client_id = :clientID "
        )
            .bind("clientID", clientID)
            .mapTo<PlanOfClient>()
            .toList()

        return ClientOfMonitor(
            id = clientID,
            name = clientInfo.name,
            email = clientInfo.email,
            weight = clientInfo.weight,
            height = clientInfo.height,
            physicalCondition = clientInfo.physicalCondition,
            birthDate = clientInfo.birthDate,
            plans = plans
        )
    }

    private data class ClientInfo(
        val name: String,
        val email: String,
        val weight: Int? = null,
        val height: Int? = null,
        val physicalCondition: String? = null,
        val birthDate: LocalDate? = null
    )

    override fun getMonitor(monitorID: UUID): MonitorDetails? =
        handle.createQuery("select id, name, email from dbo.monitors m inner join dbo.users u on u.id = m.m_id where m.m_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<MonitorDetails>()
            .singleOrNull()

    override fun getClientsOfMonitor(monitorID: UUID): List<ClientInformation> =
        handle.createQuery("select u.id,u.name, u.email from dbo.users u inner join dbo.client_to_monitor cm on u.id = cm.client_id where cm.monitor_id = :monitorID ")
            .bind("monitorID", monitorID)
            .mapTo<ClientInformation>()
            .toList()

    override fun getMonitorOfClient(clientID: UUID): MonitorDetails? {
        val monitorId = handle.createQuery(
            """
                select monitor_id from dbo.client_to_monitor where client_id = :clientID
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .mapTo<UUID>()
            .singleOrNull() ?: return null

        return getMonitor(monitorId)
    }

    override fun getMonitorRating(monitorID: UUID): Rating =
        handle.createQuery(
            "SELECT avg(stars) AS averageStarts, count(*) AS nrOfReviews FROM dbo.monitor_rating WHERE monitor_id = :monitorID"
        )
            .bind("monitorID", monitorID)
            .mapTo<Rating>()
            .single()
            .isEmpty()

    override fun searchMonitorsAvailable(name: String?, skip: Int, limit: Int, clientID: UUID): List<MonitorAvailable> {
        val queryName = if (name != null) "and u.name like :name" else ""

        return handle.createQuery(
            """
                    select id, name, email,exists(select * from dbo.monitor_requests where monitor_id = da.monitor_id and client_id = :clientID) as requested 
                    from dbo.monitors m
                    inner join dbo.users u on u.id = m.m_id
                    inner join dbo.docs_authenticity da on m.m_id = da.monitor_id
                    where da.state = 'valid' $queryName
                    offset :skip
                    limit :limit
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .bind("name", "%$name%")
            .bind("skip", skip)
            .bind("limit", limit)
            .mapTo<MonitorAvailable>()
            .toList()
    }

    override fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, decision: Boolean) {
        handle.createUpdate("delete from dbo.monitor_requests where request_id = :requestID ")
            .bind("requestID", requestID)
            .execute()

        if (decision) {
            handle.createUpdate("insert into dbo.client_to_monitor values (:monitorID,:clientID)")
                .bind("monitorID", monitorID)
                .bind("clientID", clientID)
                .execute()
        }
    }

    override fun getRequestInformation(requestID: UUID): RequestInformation? =
        handle.createQuery(
            """
            select request_id, request_text, client_id, u.name, u.email from dbo.monitor_requests 
            inner join dbo.clients c on c.c_id = monitor_requests.client_id
            inner join dbo.users u on u.id = c.c_id
            where request_id = :requestID
            """.trimIndent()
        )
            .bind("requestID", requestID)
            .mapTo<RequestInformation>()
            .singleOrNull()

    override fun monitorRequests(monitorID: UUID): List<RequestInformation> =
        handle.createQuery("select mr.request_id,mr.request_text,mr.client_id, u.name,u.email from dbo.monitor_requests mr inner join dbo.users u on u.id = mr.client_id where monitor_id = :monitorID")
            .bind("monitorID", monitorID)
            .mapTo<RequestInformation>()
            .toList()

    override fun checkIfMonitorIsVerified(monitorID: UUID): Boolean =
        handle.createQuery("select count(*) from dbo.docs_authenticity where monitor_id = :monitorID and state = 'valid' ")
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun isMonitorOfClient(monitorID: UUID, clientID: UUID): Boolean =
        handle.createQuery("select count(*) from dbo.client_to_monitor where monitor_id = :monitorID and client_id = :clientID")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .mapTo<Int>()
            .single() == 1

    override fun exercisesOfClients(monitorID: UUID, date: LocalDate): List<ClientExercises> {
        val clients = handle.createQuery(
            "select u.id,u.name from dbo.client_to_monitor ctm " +
                "inner join dbo.monitors m on m.m_id = ctm.monitor_id " +
                "inner join dbo.users u on ctm.client_id = u.id where m.m_id = :monitorID"
        )
            .bind("monitorID", monitorID)
            .mapTo<ClientData>()
            .toList()
            .ifEmpty { return emptyList() }

        return clients.map { client ->
            ClientExercises(id = client.id, name = client.name, exercises = getDailyExerciseOfClient(clientID = client.id, date = date))
        }.filter { it.exercises.isNotEmpty() }
    }

    private data class ClientData(val id: UUID, val name: String)

    private fun getDailyExerciseOfClient(clientID: UUID, date: LocalDate): List<DailyExercise> {
        val planInfo = handle.createQuery("select cp.plan_id,cp.dt_start from dbo.client_plans cp where client_id = :clientID and :date between cp.dt_start and cp.dt_end")
            .bind("clientID", clientID)
            .bind("date", date)
            .mapTo<PlanInfo>()
            .singleOrNull() ?: return emptyList()

        val dayIndex = Duration.between(planInfo.dtStart.atStartOfDay(), date.atStartOfDay()).toDays().toInt()

        val dailyListID =
            handle.createQuery("select id from dbo.daily_lists where index = :dayIndex and plan_id = :planID")
                .bind("dayIndex", dayIndex)
                .bind("planID", planInfo.id)
                .mapTo<Int>().single()

        return handle.createQuery(
            """
                select de.id,de.ex_id,dl.plan_id as planID, dl.id as dailyListID,title, description, type, sets, reps,
                    case when ev.dt_submit is null then 0 else 1 end as is_done
                from dbo.daily_exercises de
                inner join dbo.daily_lists dl on de.daily_list_id = dl.id 
                inner join dbo.exercises_info ei on ei.id = de.ex_id
                left join dbo.exercises_video ev on de.id = ev.ex_id
                where daily_list_id = :dailyListID
            """.trimIndent()
        )
            .bind("dailyListID", dailyListID)
            .mapTo<DailyExercise>()
            .toList()
    }

    private data class PlanInfo(@ColumnName("plan_id") val id: Int, @ColumnName("dt_start") val dtStart: LocalDate)
}
