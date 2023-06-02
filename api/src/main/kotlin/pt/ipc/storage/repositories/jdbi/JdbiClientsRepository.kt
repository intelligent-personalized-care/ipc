package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Client
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.exceptions.UserNotExists
import pt.ipc.storage.repositories.ClientsRepository
import java.time.LocalDate
import java.util.*

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

    override fun roleOfUser(id: UUID): Role {
        val maybeClient = handle.createQuery("select count(*) from dbo.clients where c_id = :id")
            .bind("id", id)
            .mapTo<Int>()
            .singleOrNull()

        if (maybeClient != 0) return Role.CLIENT

        handle.createQuery("select count(*) from dbo.monitors where m_id = :id")
            .bind("id", id)
            .mapTo<Int>()
            .singleOrNull() ?: throw UserNotExists

        return Role.MONITOR
    }

    override fun requestMonitor(requestID: UUID, monitorID: UUID, clientID: UUID, requestText: String?) {
        handle.createUpdate("insert into dbo.client_requests (monitor_id, client_id, request_id, request_text) VALUES (:monitorID,:clientID,:requestID,:requestText)")
            .bind("monitorID", monitorID)
            .bind("clientID", clientID)
            .bind("requestID", requestID)
            .bind("requestText", requestText)
            .execute()
    }

    override fun getUserByToken(token: String): Pair<User, Role>? {
        val user = handle.createQuery("select id, name, email, password_hash from dbo.users u inner join dbo.tokens t on u.id = t.user_id where token_hash = :token")
            .bind("token", token)
            .mapTo<User>()
            .singleOrNull() ?: return null

        val role = roleOfUser(id = user.id)

        return Pair(user, role)
    }

    override fun registerClient(input: Client, token: String, physicalCondition: String?) {
        handle.createUpdate("insert into dbo.users (id, name, email, password_hash) values (:id,:u_name,:u_email,:password_hash)")
            .bind("id", input.id)
            .bind("u_name", input.name)
            .bind("u_email", input.email)
            .bind("password_hash", input.id)
            .execute()

        handle.createUpdate(
            "insert into dbo.clients (c_id, physical_condition, weight, height, birth_date) values (:c_id,:physical_condition,:weight,:height,:birth_date)"
        )
            .bind("c_id", input.id)
            .bind("physical_condition", physicalCondition)
            .bind("weight", input.weight)
            .bind("height", input.height)
            .bind("birth_date", input.birthDate)
            .execute()

        handle.createUpdate("insert into dbo.tokens (token_hash, user_id) values (:token_hash, :user_id)")
            .bind("token_hash", token)
            .bind("user_id", input.id)
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
            "select count(*) from dbo.plans p " +
                "inner join dbo.daily_lists dl on p.id = dl.plan_id " +
                "inner join dbo.daily_exercises de on dl.id = de.daily_list_id " +
                "inner join dbo.client_plans cp on cp.plan_id = p.id " +
                "where p.id = :planID and dl.id = :dailyListID and de.id = :exerciseID and cp.client_id = :clientID "
        )
            .bind("planID", planID)
            .bind("dailyListID", dailyList)
            .bind("exerciseID", exerciseID)
            .bind("clientID", clientID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun checkIfClientAlreadyUploadedVideo(clientID: UUID, exerciseID: Int): Boolean {
        return handle.createQuery("select count(*) from dbo.exercises_video where client_id = :client and ex_id = :exerciseID")
            .bind("client", clientID)
            .bind("exerciseID", exerciseID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun uploadExerciseVideoOfClient(
        clientID: UUID,
        exerciseID: Int,
        exerciseVideoID: UUID,
        date: LocalDate,
        clientFeedback: String?
    ) {
        handle.createUpdate(
            "insert into dbo.exercises_video (id, ex_id, client_id, dt_submit, feedback_client, feedback_monitor) " +
                "VALUES (:exerciseVideoID,:exerciseID,:clientID,:date,:clientFeedback,null)"
        )
            .bind("exerciseVideoID", exerciseVideoID)
            .bind("exerciseID", exerciseID)
            .bind("clientID", clientID)
            .bind("date", date)
            .bind("clientFeedback", clientFeedback)
            .execute()
    }
}
