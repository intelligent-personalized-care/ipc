package pt.ipc.http.utils

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import pt.ipc.http.models.emitter.EmitterModel
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

@Component
class SseEmitterRepository {

    private val nonBlockingService = Executors.newCachedThreadPool()

    private val emitters = ConcurrentHashMap<UUID, SseEmitter>()

    fun createConnection(userID: UUID): SseEmitter {
        val emitter = SseEmitter(0)

        emitters.put(userID, emitter)?.complete()

        send(userID = userID, object: EmitterModel() { val accept = "Connection Accepted" })

        return emitter
    }

    fun send(userID: UUID, obj: EmitterModel) {
        val emitter = emitters[userID] ?: return

        nonBlockingService.execute {
            try {
                emitter.send(event().id(obj::class.java.simpleName).data(obj))
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
