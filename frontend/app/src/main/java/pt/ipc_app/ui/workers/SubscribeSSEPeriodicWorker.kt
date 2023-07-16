package pt.ipc_app.ui.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.IPCApplication.Companion.SSE_NOTIFICATIONS_CHANNEL
import pt.ipc_app.R
import pt.ipc_app.service.models.sse.SseEvent
import pt.ipc_app.service.models.sse.getMessage
import pt.ipc_app.service.sse.EventBus
import pt.ipc_app.service.sse.SseEventListener
import kotlin.random.Random

class SubscribeSSEPeriodicWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), SseEventListener {

    override suspend fun doWork(): Result {
        EventBus.registerListener(this)

        val app = (applicationContext as DependenciesContainer)

        return try {
            app.services.sseService.start(
                app.sessionManager.userLoggedIn.accessToken
            )

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    override fun onEventReceived(eventData: SseEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            eventData.getMessage()?.let {
                startForegroundService(it.first, it.second)
                delay(NOTIFICATION_DELAY)
            }
        }
    }

    private suspend fun startForegroundService(title: String, message: String) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(applicationContext, SSE_NOTIFICATIONS_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .build()
            )
        )
    }

    companion object {
        private const val NOTIFICATION_DELAY = 5000L
    }
}