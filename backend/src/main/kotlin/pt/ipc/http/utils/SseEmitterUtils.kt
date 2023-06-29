package pt.ipc.http.utils

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors


@Component
class SseEmitterUtils {

    private val nonBlockingService = Executors.newCachedThreadPool()

    private val emitters = ConcurrentHashMap<UUID, SseEmitter>()


    fun createConnection(userID : UUID) : SseEmitter{
        val emitter = SseEmitter(0)
        emitters[userID] = emitter
        return emitter
    }

    fun send(userID: UUID, obj : Any){

        nonBlockingService.execute {
            val emitter = emitters[userID] ?: throw Exception("This Emitter does not exists")
            try{
                emitter.send(
                    obj
                )
            }catch (ex : Exception){
                emitter.completeWithError(ex)
            }
        }


    }

    fun endConnection(userID: UUID){
        val emitter = emitters[userID] ?: throw Exception("This Emitter does not exists")
        emitter.complete()
        emitters.remove(userID)
    }



}

