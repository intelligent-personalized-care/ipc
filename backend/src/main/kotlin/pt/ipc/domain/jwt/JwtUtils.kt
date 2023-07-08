package pt.ipc.domain.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import pt.ipc.domain.Role
import pt.ipc.domain.toRole
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(jwtConfiguration: JwtConfiguration) {

    private val expirationDate = 8L

    private val sessionID = "sessionID"
    private val userID = "userID"
    private val userRole = "role"

    private val accessTokenKey = SecretKeySpec(
        jwtConfiguration.accessTokenSecret.toByteArray(),
        SECRET_KEY_ALGORITHM
    )

    private data class JwtPayload(val claims: Claims)

    private fun createJwtPayload(id: UUID, role: Role): JwtPayload {
        val claims = Jwts.claims()
        claims[userID] = id
        claims[userRole] = role
        claims[sessionID] = UUID.randomUUID()
        return JwtPayload(claims = claims)
    }

    fun createJWToken(id: UUID, role: Role): String {
        val jwtPayload = createJwtPayload(id = id, role = role)

        return JWToken(
            token = Jwts.builder()
                .setClaims(jwtPayload.claims)
                .signWith(accessTokenKey)
                .setExpiration(Date.from(Instant.now().plus(expirationDate, ChronoUnit.HOURS)))
                .compact()
        ).token
    }

    fun getUserInfo(token: String): Pair<UUID, Role> {
        val claims = getClaimsOfToken(token = token)

        val id = UUID.fromString(claims[userID].toString())
        val role = claims[userRole].toString().toRole()

        return Pair(first = id, second = role)
    }

    private fun getClaimsOfToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(accessTokenKey)
            .build()
            .parseClaimsJws(token).body
    }

    companion object {
        private const val SECRET_KEY_ALGORITHM = "HmacSHA512"
    }
}
