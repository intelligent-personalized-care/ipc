package pt.ipc_app.ui.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.service.connection.APIResult
import java.io.File

class VideoSubmissionOneTimeWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = (applicationContext as DependenciesContainer)

        return try {
            val filePath = inputData.getString(WorkerKeys.FILE_PATH) ?: throw IllegalArgumentException()
            val planId = inputData.getString(WorkerKeys.PLAN_ID) ?: throw IllegalArgumentException()
            val dailyListId = inputData.getString(WorkerKeys.DAILY_LIST_ID) ?: throw IllegalArgumentException()
            val exerciseId = inputData.getString(WorkerKeys.EXERCISE_ID) ?: throw IllegalArgumentException()
            val nrSet = inputData.getString(WorkerKeys.NR_SET) ?: throw IllegalArgumentException()

            val res = app.services.exercisesService.submitExerciseVideo(
                video = File(filePath),
                clientId = app.sessionManager.userUUID,
                planId = planId.toInt(),
                dailyListId = dailyListId.toInt(),
                exerciseId = exerciseId.toInt(),
                set = nrSet.toInt(),
                token = app.sessionManager.userLoggedIn.token
            )

            when (res) {
                is APIResult.Success -> {
                    Result.success()
                }
                is APIResult.Failure -> {
                    Result.failure()
                }
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}