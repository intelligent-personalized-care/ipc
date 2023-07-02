package pt.ipc.http.utils

import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors


@Component
class SseEmitterUtils {

    private class AcceptConnection(val accept : String = "Connection Accepted")
    private val accept = AcceptConnection()

    private data class Object(val className : String, val obj : Any){
        init{
            require(obj::class.simpleName != null)
        }
    }

    private val nonBlockingService = Executors.newCachedThreadPool()

    private val emitters = ConcurrentHashMap<UUID, SseEmitter>()

    fun createConnection(userID : UUID) : SseEmitter{
        val emitter = SseEmitter(0)
        emitters[userID] = emitter
        send(userID = userID, accept )
        return emitter
    }

    fun send(userID: UUID, obj : Any){

        val emitter = emitters[userID] ?: return

        val newObject = Object(
            className = obj::class.simpleName ?: throw IllegalArgumentException("Class can not be anonymous"),
            obj = obj
        )

        nonBlockingService.execute {
            try{
                emitter.send(newObject)
            }catch (ex : Exception){
                emitter.completeWithError(ex)
            }
        }
    }

    fun endConnection(userID: UUID){
        val emitter = emitters[userID] ?: return
        emitter.complete()
        emitters.remove(userID)
    }



}

