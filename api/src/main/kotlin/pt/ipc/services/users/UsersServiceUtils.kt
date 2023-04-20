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

    fun getUserByToken(token: String): Pair<User,Role>? {
        val hashedToken = encryptionUtils.encrypt(token)
        return transactionManager.runBlock(
            {
                it.clientsRepository.getUserByToken(token = hashedToken)
            }
        )
    }

    fun createCredentials(email: String, role : Role) : Pair<String, UUID>{

        val id = UUID.randomUUID()

        val token = jwtUtils.createJWToken(email = email, id = id, role = role)

        return Pair(token.token, id)
    }


    private fun isPasswordSafe(password: String) : Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$")
        return regex.matches(password)
    }

    private fun emailExists(email: String) : Boolean =
        transactionManager.runBlock(
            {
                it.clientsRepository.existsEmail(email = email)
            }
    )

    fun checkDetails(email: String, password: String){
        if(!email.contains("@")) throw BadEmail()
        if(!isPasswordSafe(password = password)) throw WeakPassword()
        if(emailExists(email = email)) throw EmailAlreadyInUse()
    }


}