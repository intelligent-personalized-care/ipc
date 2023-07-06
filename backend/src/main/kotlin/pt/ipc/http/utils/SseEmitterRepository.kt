package pt.ipc.http.utils

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

@Component
class SseEmitterRepository {

    private class AcceptConnection(val accept: String = "Connection Accepted")

    private val accept = AcceptConnection()

    private data class SenderObject(val className: String, val obj: Any) {
        init {
            require(obj::class.simpleName != null)
        }
    }

    private val nonBlockingService = Executors.newCachedThreadPool()

    private val emitters = ConcurrentHashMap<UUID, SseEmitter>()

    fun createConnection(userID: UUID): SseEmitter {
        val emitter = SseEmitter(0)
        emitters.put(userID, emitter)?.complete()
        send(userID = userID, accept)
        return emitter
    }

    fun send(userID: UUID, obj: Any) {
        val emitter = emitters[userID] ?: return

        val newSenderObject = SenderObject(
            className = obj::class.simpleName ?: throw IllegalArgumentException("Class can not be anonymous"),
            obj = obj
        )

        nonBlockingService.execute {
            try {
                emitter.send(newSenderObject)
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }
    }

    fun endConnection(userID: UUID) {
        val emitter = emitters[userID] ?: return
        emitter.complete()
        emitters.remove(userID)
    }
}
