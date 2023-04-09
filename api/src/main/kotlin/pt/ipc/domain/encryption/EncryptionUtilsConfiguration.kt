package pt.ipc.domain.encryption

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class EncryptionUtilsConfiguration(
    @Value("\${server.config.secrets.encryption-secret}")
    val encryptionSecret: String
)