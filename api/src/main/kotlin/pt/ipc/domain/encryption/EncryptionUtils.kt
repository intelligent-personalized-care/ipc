package pt.ipc.domain.encryption

import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class EncryptionUtils(
    encryptionUtilsConfiguration: EncryptionUtilsConfiguration
) {

    private val secretKey = encryptionUtilsConfiguration.encryptionSecret

    fun encrypt(plainText: String): String {

        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(secretKey.toByteArray(), "AES"))

        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    fun decrypt(encryptedText: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secretKey.toByteArray(), "AES"))
        val encryptedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

}