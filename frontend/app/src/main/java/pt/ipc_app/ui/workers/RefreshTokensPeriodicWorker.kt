package pt.ipc_app.ui.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.connection.APIResult

class RefreshTokensPeriodicWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = (applicationContext as DependenciesContainer)

        return try {
            val res = app.services.usersService.refreshTokens(
                app.sessionManager.userLoggedIn.accessToken
            )

            when (res) {
                is APIResult.Success -> {
                    app.sessionManager.updateTokens(res.data.accessToken, res.data.refreshToken)
                    Result.success()
                }
                is APIResult.Failure -> {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

}
