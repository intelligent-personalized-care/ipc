package pt.ipc.database_storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Client
import pt.ipc.database_storage.repositories.ClientsRepository
import pt.ipc.domain.User


class JdbiClientsRepository(
    private val handle : Handle
) : ClientsRepository {

    override fun existsEmail(email: String): Boolean {
        return handle
            .createQuery("select count(*) from dbo.users where u_email = :email")
            .bind("email", email)
            .mapTo<Int>()
            .single() == 1
    }

    override fun getUserByToken(token: String): User? {
        TODO("Not yet implemented")
    }

    override fun registerClient(input: Client, token: String, physicalCondition : String){

        handle.createUpdate("insert into dbo.users (id, u_name, u_email, password_hash) values (:id,:u_name,:u_email,:password_hash)")
            .bind("id", input.id )
            .bind("u_name", input.name )
            .bind("u_email", input.email )
            .bind("password_hash", input.id )
            .execute()

        handle.createUpdate(
            "insert into dbo.client(c_id, physical_condition, weigth, heigth, birth_date, monitor_id) values (:c_id,:physical_condition,:weigth,:heigth,:birth_date,null)"
        )
            .bind("c_id", input.id)
            .bind("physical_condition", physicalCondition)
            .bind("weigth", input.weigth)
            .bind("heigth", input.heigth)
            .bind("birth_date", input.birthDate)
            .execute()

        handle.createUpdate("insert into dbo.tokens (token_hash, user_id) values (:token_hash, :user_id)")
            .bind("token_hash", token)
            .bind("user_id", input.id)
            .execute()


    }



}

