package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import java.io.File
import java.io.IOException
import java.util.*

/**
 * The service that handles the exercises functionalities.
 *
 * @property apiEndpoint the API endpoint
 * @property httpClient the HTTP client
 * @property jsonEncoder the JSON encoder used to serialize/deserialize objects
 */
class ExercisesService(
    apiEndpoint: String,
    httpClient: OkHttpClient,
    jsonEncoder: Gson
) : HTTPService(apiEndpoint, httpClient, jsonEncoder) {

    /**
     * Submit an exercise video of Client.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun submitExerciseVideo(
        video: File,
        clientId: UUID,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int,
        token: String
    ): APIResult<FileOutput> =
        postWithFile(
            uri = "/users/clients/$clientId/plans/$planId/daily_lists/$dailyListId/exercises/$exerciseId",
            token = token,
            multipartPropName = "video",
            file = video,
            contentType = "video/mp4"
        )

}