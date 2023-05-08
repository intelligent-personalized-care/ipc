package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Client
import pt.ipc.domain.RequestDecision
import pt.ipc.domain.RequestInformation
import pt.ipc.domain.Role
import pt.ipc.domain.User
import pt.ipc.domain.UserNotExists
import pt.ipc.storage.repositories.ClientsRepository
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

    override fun decideRequest(requestID: UUID, clientID: UUID, monitorID: UUID, decision: RequestDecision) {
        handle.createUpdate("delete from dbo.client_requests where request_id = :requestID ")
            .bind("requestID", requestID)
            .execute()

        if (decision == RequestDecision.ACCEPT) {
            handle.createUpdate("insert into dbo.client_to_monitor values (:monitorID,:clientID)")
                .bind("monitorID", monitorID)
                .bind("clientID", clientID)
                .execute()
        }
    }

    override fun getRequestInformations(requestID: UUID): RequestInformation? {
        return handle.createQuery("select * from dbo.client_requests where request_id = :requestID ")
            .bind("requestID", requestID)
            .mapTo<RequestInformation>()
            .singleOrNull()
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

    override fun updateProfilePictureID(userID: UUID, profileID: UUID) {
        handle.createUpdate("update dbo.users set photo_id = :profileID where id = :userID")
            .bind("profileID", profileID)
            .bind("userID", userID)
            .execute()
    }

    override fun getClientRequests(clientID: UUID): List<RequestInformation> =
        handle.createQuery("select client_id, monitor_id, request_id from dbo.client_requests where client_id = :clientID")
            .bind("clientID", clientID)
            .mapTo<RequestInformation>()
            .toList()
}
