package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.client.Client
import pt.ipc.domain.client.ClientOutput
import pt.ipc.storage.repositories.ClientsRepository
import java.time.LocalDate
import java.util.UUID

class JdbiClientsRepository(
    private val handle: Handle
) : ClientsRepository {

    override fun existsEmail(email: String): Boolean {
        return handle
            .createQuery("select count(*) from dbo.users where email = :email")
            .bind("email", email)
            .mapTo<Int>()
            .single() == 1
    }

    override fun getClient(clientID: UUID): ClientOutput? =
        handle.createQuery(
            "select u.id, u.name, u.email, c.weight, c.height, c.physical_condition as physicalCondition, c.birth_date as birthDate " +
                "from dbo.clients c inner join dbo.users u on c.c_id = u.id  where c.c_id = :clientID"
        )
            .bind("clientID", clientID)
            .mapTo<ClientOutput>()
            .singleOrNull()

    override fun requestMonitor(requestID: UUID, monitorID: UUID, clientID: UUID, requestText: String?) {
        handle.createUpdate("insert into dbo.monitor_requests (monitor_id, client_id, request_id, request_text) values (:monitorID,:clientID,:requestID,:requestText)")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .bind("requestID", requestID)
            .bind("requestText", requestText)
            .execute()
    }

    override fun deleteConnection(monitorID: UUID, clientID: UUID) {
        handle.createUpdate("delete from dbo.client_to_monitor where client_id = :clientID and monitor_id = :monitorID")
            .bind("clientID", clientID)
            .bind("monitorID", monitorID)
            .execute()
    }

    override fun registerClient(input: Client, sessionID: String) {
        handle.createUpdate("insert into dbo.users (id, name, email, password_hash) values (:id,:u_name,:u_email,:password_hash)")
            .bind("id", input.id)
            .bind("u_name", input.name)
            .bind("u_email", input.email)
            .bind("password_hash", input.password)
            .execute()

        handle.createUpdate(
            "insert into dbo.clients (c_id, physical_condition, weight, height, birth_date) values (:c_id,:physical_condition,:weight,:height,:birth_date)"
        )
            .bind("c_id", input.id)
            .bind("physical_condition", input.physicalCondition)
            .bind("weight", input.weight)
            .bind("height", input.height)
            .bind("birth_date", input.birthDate)
            .execute()

        handle.createUpdate("insert into dbo.session(user_id, session) values(:userID, :sessionID)")
            .bind("userID", input.id)
            .bind("sessionID", sessionID)
            .execute()
    }

    override fun hasClientRatedMonitor(clientID: UUID, monitorID: UUID): Boolean =
        handle.createQuery("select count(*) from dbo.monitor_rating where client_id = :clientID and monitor_id = :monitorID ")
            .bind("clientID", clientID)
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun rateMonitor(clientID: UUID, monitorID: UUID, rating: Int) {
        handle.createUpdate("insert into dbo.monitor_rating (monitor_id, client_id, stars) values (:monitorID,:clientID, :rating)")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .bind("rating", rating)
            .execute()
    }

    override fun checkIfClientHasThisExercise(clientID: UUID, planID: Int, dailyList: Int, exerciseID: Int): Boolean {
        return handle.createQuery(
            """
            select exists(
                select * from dbo.plans p
                inner join dbo.daily_lists dl on p.id = dl.plan_id
                inner join dbo.daily_exercises de on dl.id = de.daily_list_id
                inner join dbo.client_plans cp on cp.plan_id = p.id
                where p.id = :planID and dl.id = :dailyListID and de.id = :exerciseID and cp.client_id = :clientID
            )
            """.trimIndent()
        )
            .bind("planID", planID)
            .bind("dailyListID", dailyList)
            .bind("exerciseID", exerciseID)
            .bind("clientID", clientID)
            .mapTo<Boolean>()
            .single()
    }

    override fun checkIfClientAlreadyUploadedVideo(clientID: UUID, exerciseID: Int, set: Int): Boolean {
        return handle.createQuery("select count(*) from dbo.exercises_video where client_id = :client and ex_id = :exerciseID and nr_set = :set")
            .bind("client", clientID)
            .bind("exerciseID", exerciseID)
            .bind("set", set)
            .mapTo<Int>()
            .single() == 1
    }

    override fun uploadExerciseVideoOfClient(
        clientID: UUID,
        exerciseID: Int,
        exerciseVideoID: UUID,
        date: LocalDate,
        set: Int,
        clientFeedback: String?
    ): Boolean {
        handle.createUpdate(
            "insert into dbo.exercises_video (id, ex_id, client_id, dt_submit, feedback_client, feedback_monitor,nr_set) " +
                "VALUES (:exerciseVideoID,:exerciseID,:clientID,:date,:clientFeedback,null,:set)"
        )
            .bind("exerciseVideoID", exerciseVideoID)
            .bind("exerciseID", exerciseID)
            .bind("clientID", clientID)
            .bind("date", date)
            .bind("clientFeedback", clientFeedback)
            .bind("set", set)
            .execute()

        return handle.createQuery(
            "select " +
                "case when count(ev.ex_id) != de.sets then 0 else 1 end " +
                "from dbo.exercises_video ev " +
                "inner join dbo.daily_exercises de on ev.ex_id = de.id where ev.ex_id = :exerciseID " +
                "GROUP BY de.sets"
        )
            .bind("exerciseID", exerciseID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun getClientsVideosIDs(): List<UUID> =
        handle.createQuery("select id from dbo.exercises_video").mapTo<UUID>().toList()

    override fun deleteClientVideoID(videoID: UUID) {
        handle.createUpdate("delete from dbo.exercises_video where id = :videoID")
            .bind("videoID", videoID)
            .execute()
    }
}
