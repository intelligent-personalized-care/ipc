package pt.ipc.database_storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Client
import pt.ipc.database_storage.repositories.ClientsRepository
import pt.ipc.domain.Role
import pt.ipc.domain.User

class JdbiClientsRepository(
    private val handle : Handle
) : ClientsRepository {

    override fun existsEmail(email: String): Boolean {
        return handle
            .createQuery("select count(*) from dbo.users where email = :email")
            .bind("email", email)
            .mapTo<Int>()
            .single() == 1
    }

    override fun getUserByToken(token: String): User? {

        return handle.createQuery("select id, name, email, password_hash from dbo.users u inner join dbo.tokens t on u.id = t.user_id where token_hash = :token")
            .bind("token", token)
            .mapTo<User>()
            .singleOrNull()
    }

    override fun registerClient(input: Client, token: String, physicalCondition : String?){

        handle.createUpdate("insert into dbo.users (id, name, email, password_hash) values (:id,:u_name,:u_email,:password_hash)")
            .bind("id", input.id )
            .bind("u_name", input.name )
            .bind("u_email", input.email )
            .bind("password_hash", input.id )
            .execute()

        handle.createUpdate(
            "insert into dbo.clients (c_id, physical_condition, weight, height, birth_date, monitor_id) values (:c_id,:physical_condition,:weight,:height,:birth_date,null)"
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

    override fun roleOfUser(token: String) : Role {

        handle.createQuery("select count(*) from dbo.tokens t inner join dbo.clients c on t.user_id = c.c_id where t.token_hash = :token")
            .bind("token", token)
            .mapTo<Int>()
            .singleOrNull() ?: return Role.CLIENT

        return Role.MONITOR
    }

}

