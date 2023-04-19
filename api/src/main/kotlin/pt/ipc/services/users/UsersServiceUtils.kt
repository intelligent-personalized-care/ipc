package pt.ipc.services.users

import org.springframework.stereotype.Component
import pt.ipc.database_storage.artificialTransaction.TransactionManager
import pt.ipc.domain.*
import pt.ipc.domain.encryption.EncryptionUtils
import pt.ipc.domain.jwt.JwtUtils
import java.util.*

@Component
class UsersServiceUtils(
    private val encryptionUtils: EncryptionUtils,
    private val transactionManager: TransactionManager,
    private val jwtUtils: JwtUtils,
) {

    fun getUserByToken(token: String): User? {
        TODO("Not yet implemented")
    }

    fun createCredentials(email: String, name : String) : Pair<String, UUID>{

        val token = jwtUtils.createJWToken(email = email, name = name )

        return Pair(token.token, UUID.randomUUID())
    }


    private fun isPasswordSafe(password: String) : Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$")
        return regex.matches(password)
    }

    private fun emailExists(email: String) : Boolean =
        transactionManager.runBlock({
            it.clientsRepository.existsEmail(email = email)
        }

    )

    fun checkDetails(email: String, password: String){
        if(!email.contains("@")) throw BadEmail()
        if(!isPasswordSafe(password = password)) throw WeakPassword()
        if(emailExists(email = email)) throw EmailAlreadyInUse()
    }


}