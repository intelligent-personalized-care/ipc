package pt.ipc.services.users

import org.springframework.stereotype.Service
import pt.ipc.domain.BadEmail
import pt.ipc.domain.EmailAlreadyInUse
import pt.ipc.domain.User
import pt.ipc.domain.User.Companion.isPasswordSafe
import pt.ipc.domain.WeakPassword
import pt.ipc.http.jwt.JwtUtils
import pt.ipc.repositories.TransactionManager
import pt.ipc.services.users.dtos.RegisterInput
import pt.ipc.services.users.dtos.RegisterOutput
import pt.ipc.services.users.encryption.EncryptionUtils
import java.util.*

@Service
class UsersServiceImpl(
   private val transactionManager: TransactionManager,
   private val jwtUtils: JwtUtils,
   private val encryptionUtils: EncryptionUtils

): UsersService {

    override fun getUserByToken(token: String): User? {
        TODO("Not yet implemented")
    }


    private fun emailExists(email: String) : Boolean =
        transactionManager.run{
            val encrypted = encryptionUtils.encrypt(plainText = email)
            it.usersRepository.existsEmail(email = encrypted)
        }

    private fun checkDetails(email: String, password: String){
        if(!email.contains("@")) throw BadEmail()
        if(!isPasswordSafe(password)) throw WeakPassword()
        if(emailExists(email)) throw EmailAlreadyInUse()
    }

    override fun register(input: RegisterInput): RegisterOutput {
        checkDetails(email = input.email, password = input.password)

        val token = jwtUtils.createJWToken(input.email, input.email)

        val encryptedToken = encryptionUtils.encrypt(token.token)

        val id = UUID.randomUUID()

        val user = User(
            id = id,
            name = input.name,
            email = input.email,
            password = input.password,
            weigth = input.weigth,
            heigth = input.heigth,
            birthDate = input.birthDate
        )

        val encryptedUser = user.copy(
            password = encryptionUtils.encrypt(user.password)
        )

        transactionManager.run{
            it.usersRepository.register(encryptedUser, encryptedToken,input.physicalCondition)
        }

        return RegisterOutput(id = id, token = token.token)

    }

}