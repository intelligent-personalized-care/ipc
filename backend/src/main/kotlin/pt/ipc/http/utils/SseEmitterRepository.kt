package pt.ipc.http.utils

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import pt.ipc.http.models.emitter.EmitterModel
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class SseEmitterRepository {

    private val nonBlockingService = Executors.newCachedThreadPool()

    private val emitters = ConcurrentHashMap<UUID, SseEmitter>()

    private val usages = ConcurrentHashMap<UUID, LocalDateTime>()

    fun createConnection(userID: UUID): SseEmitter {
        val emitter = SseEmitter(-1L)

        emitters.put(userID, emitter)?.complete()

        send(userID = userID, obj = object : EmitterModel() { val accept = "Connection Accepted" })

        return emitter
    }

    fun send(userID: UUID, obj: EmitterModel) {
        val emitter = emitters[userID] ?: return

        nonBlockingService.execute {
            try {
                emitter.send(event().id(obj::class.java.simpleName).data(obj))
                usages[userID] = LocalDateTime.now()
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

    @Scheduled(fixedDelay = EMITTERS_CLEANUP_INTERVAL, timeUnit = TimeUnit.HOURS)
    private fun removeExpiredEmitters() {

        for ((user, date) in usages) {
            val currentDateTime = LocalDateTime.now()

            val dateLimit = date.plusHours(MAX_HOURS)

            if (currentDateTime.isAfter(dateLimit)) {
                emitters[user]?.complete()
                emitters.remove(user)
                usages.remove(user)
            }
        }
    }

    companion object {
        const val EMITTERS_CLEANUP_INTERVAL =  8L // 10 minutes
        const val MAX_HOURS = 12L
    }
}
