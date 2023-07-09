package pt.ipc_app.service

import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.exercises.ListOfExercisesInfo
import pt.ipc_app.service.utils.ContentType
import pt.ipc_app.service.utils.MultipartEntry
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

    fun getExerciseVideoOfClientUrl(
        clientId: String,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int
    ): String =
        "$apiEndpoint/users/clients/$clientId/plans/$planId/daily_lists/$dailyListId/exercises/$exerciseId?set=1"

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
        set: Int,
        token: String
    ): APIResult<Any> =
        postWithMultipartBody(
            uri = "/users/clients/$clientId/plans/$planId/daily_lists/$dailyListId/exercises/$exerciseId",
            token = token,
            multipartEntries = listOf(
                MultipartEntry(name = "video", value = video, contentType = ContentType.VIDEO),
                MultipartEntry(name = "set", value = set)
            )
        )

}