package pt.ipc.domain.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfiguration(
    @Value("\${server.config.secrets.access-token-secret}")
    val accessTokenSecret: String
)
