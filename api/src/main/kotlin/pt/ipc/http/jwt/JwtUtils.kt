package pt.ipc.http.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(jwtConfiguration : JwtConfiguration) {

    private val USER_CREATION = "userCreation"

    private val acessTokenKey = SecretKeySpec(
            jwtConfiguration.accessTokenSecret.toByteArray(),
            SECRET_KEY_ALGORITHM
    )

     data class JwtPayload(val claims: Claims)

     fun createJwtPayload(email : String, name : String): JwtPayload {
                val claims = Jwts.claims()
                claims[USER_CREATION] = "${email}${name}"
                return JwtPayload(claims = claims)
     }
    

    fun createJWToken(email : String, name : String): JWToken {
        
        val jwtPayload = createJwtPayload(email = email, name = name)
        
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