package pt.ipc.domain.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.toRole
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(jwtConfiguration : JwtConfiguration) {

    private val userEmail = "userEmail"
    private val userID = "userID"
    private val userRole = "role"

    private val acessTokenKey = SecretKeySpec(
            jwtConfiguration.accessTokenSecret.toByteArray(),
            SECRET_KEY_ALGORITHM
    )

     data class JwtPayload(val claims: Claims)

     private fun createJwtPayload(email : String, id : UUID, role: Role): JwtPayload {
                val claims = Jwts.claims()
                claims[userEmail] = email
                claims[userID] = id
                claims[userRole] = role
                return JwtPayload(claims = claims)
     }
    

    fun createJWToken(email : String, id : UUID, role : Role): JWToken {
        
        val jwtPayload = createJwtPayload(email = email, id = id, role = role)
        
        return JWToken(
             token =   Jwts.builder()
                       .setClaims(jwtPayload.claims)
                       .signWith(acessTokenKey)
                       .compact()
        )
    }

    companion object {
        private const val SECRET_KEY_ALGORITHM = "HmacSHA512"
    }
}