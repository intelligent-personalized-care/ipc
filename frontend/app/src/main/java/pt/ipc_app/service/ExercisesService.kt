package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.exercises.ListOfExercisesInfo
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
     * Gets all the exercises.
     *
     * @return the API result of the request
     *
     * @throws IOException if there is an error while sending the request
     */
    suspend fun getExercises(
        skip: Int = 0,
        token: String
    ): APIResult<ListOfExercisesInfo> =
        get(
            uri = "/exercises?skip=$skip",
            token = token
        )

    fun getExercisePreviewUrl(
        exerciseInfoId: UUID
    ): String =
        "$apiEndpoint/exercises/$exerciseInfoId/video"

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
    ): APIResult<Any> =
        postWithFile(
            uri = "/users/clients/$clientId/plans/$planId/daily_lists/$dailyListId/exercises/$exerciseId",
            token = token,
            multipartPropName = "video",
            file = video,
            contentType = "video/mp4"
        )

}