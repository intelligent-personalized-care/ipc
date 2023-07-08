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

    private val accessTokenExpirationDate = 1L
    private val refreshTokenExpirationDate = 1L

    private val sessionID = "sessionID"
    private val userID = "userID"
    private val userRole = "role"

    private val accessTokenKey = SecretKeySpec(
        jwtConfiguration.accessTokenSecret.toByteArray(),
        SECRET_KEY_ALGORITHM
    )

    private data class JwtPayload(val claims: Claims)

    private fun createJwtPayload(id: UUID, role: Role, session: UUID): JwtPayload {
        val claims = Jwts.claims()
        claims[userID] = id
        claims[userRole] = role
        claims[sessionID] = session
        return JwtPayload(claims = claims)
    }

    fun createAccessToken(userID: UUID, role: Role, sessionID: UUID): String {
        val jwtPayload = createJwtPayload(id = userID, role = role, session = sessionID)

        return JWToken(
            token = Jwts.builder()
                .setClaims(jwtPayload.claims)
                .signWith(accessTokenKey)
                .setExpiration(Date.from(Instant.now().plus(accessTokenExpirationDate, ChronoUnit.HOURS)))
                .compact()
        ).token
    }

    fun createRefreshToken(session: UUID): String {
        val claims = Jwts.claims()
        claims[sessionID] = session

        return JWToken(
            token = Jwts.builder()
                .setClaims(claims)
                .signWith(accessTokenKey)
                .setExpiration(Date.from(Instant.now().plus(refreshTokenExpirationDate, ChronoUnit.DAYS)))
                .compact()
        ).token
    }

    fun getUserInfo(token: String): Triple<UUID, Role, UUID> {
        val claims = getClaimsOfToken(token = token)

        val id = UUID.fromString(claims[userID].toString())
        val role = claims[userRole].toString().toRole()
        val sessionID = UUID.fromString(claims[sessionID].toString())

        return Triple(first = id, second = role, third = sessionID)
    }

    fun getSessionID(token: String): UUID {
        val claims = getClaimsOfToken(token = token)
        return UUID.fromString(claims[sessionID].toString())
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
